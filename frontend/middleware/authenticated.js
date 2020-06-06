import { Base64 } from 'js-base64'

export default function({ store, redirect }) {
    // If the user is not authenticated
    console.log('authenticated', store.state.login.authUser.jwt !== '')
    if (store.state.login.authUser.jwt === '') {
        return redirect('/login')
    }
}
