import { Base64 } from 'js-base64'

export default function({ store, redirect }) {
    // If the user is not authenticated
    if (store.state.login.authUser.jwt === '') {
        // console.log('authenticated.js', store.state.login.authUser.jwt)
        return redirect('/login')
    }
}
