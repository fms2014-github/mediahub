export default function({ store, redirect }) {
    // If the user is authenticated redirect to home page
    console.log('notAuthenticated', store.state.login.authUser.jwt !== '')
    if (store.state.login.authUser.jwt !== '') {
        return redirect('/')
    }
}
