import express from 'express'

// Create express router
const router = express.Router()

// Transform req & res to have the same API as express
// So we can use res.status() & res.json()
const app = express()
router.use((req, res, next) => {
    Object.setPrototypeOf(req, app.request)
    Object.setPrototypeOf(res, app.response)
    req.res = res
    res.req = req
    next()
})

// Add POST - /api/login
router.post('/setSession', (req, res) => {
    console.log('req.body', req.body)
    if (req.body.jwt !== null) {
        req.session.jwt = req.body
        return res.json(req.body)
    }
    return res.status(401).json(null)
})

// Add POST - /api/logout
router.post('/deleteSession', (req, res) => {
    delete req.session.jwt
    res.json({ ok: true })
})

// Export the server middleware
export default {
    path: '/auth',
    handler: router,
}
