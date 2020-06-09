import { Base64 } from 'js-base64'

export default function({ store, redirect }) {
    if (store.state.login.authUser.jwt === '') {
        // console.log('firstLogin.js::jwtTrue', store.state.login.authUser.jwt)
        return redirect('/login')
    } else if (store.state.login.authUser.firstLogin >= 2) {
        // console.log('firstLogin.js::firstLoginTrue', store.state.login.authUser.firstLogin)
        return redirect('/')
    }
}
