import { Base64 } from 'js-base64'

export default function({ store, redirect }) {
    console.log('firstLogin.js', store.state.login.authUser)
    if (store.state.login.authUser.jwt === '') {
        return redirect('/login')
    } else if (store.state.login.authUser.firstLogin >= 2) {
        return redirect('/')
    }
}
