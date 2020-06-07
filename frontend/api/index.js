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
    if (req.body.jwt !== null) {
        console.log('/setSession::req.body.jwt::before !== null', req.session.data)
        if (req.session.data === undefined) {
            req.session.data = { jwt: req.body.jwt }
            console.log('/setSession::req.session.data === undefined', req.session.data)
        }
        if (req.session.data !== undefined) {
            req.session.data.firstLogin = req.body.firstLogin
        }
        console.log('/setSession::req.body.jwt::after !== null', req.session.data)
        return res.status(200).json(req.session.data)
    }
    return res.status(401).json(null)
})

// Add POST - /api/logout
router.post('/deleteSession', (req, res) => {
    delete req.session.data
    res.json({ ok: true })
})

// Export the server middleware
export default {
    path: '/auth',
    handler: router,
}
