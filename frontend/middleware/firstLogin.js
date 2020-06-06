import { Base64 } from 'js-base64'

export default function({ store, redirect }) {
    console.log('firstLogin', JSON.parse(Base64.decode(store.state.login.authUser.jwt.split('.')[1])).firstLogin === 1)

    if (store.state.login.authUser.jwt === '') {
        return redirect('/login')
    } else if (JSON.parse(Base64.decode(store.state.login.authUser.jwt.split('.')[1])).firstLogin === 1) {
        return redirect('/')
    }
}
