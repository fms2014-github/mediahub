export default function({ store, redirect }) {
    // If the user is authenticated redirect to home page
    if (store.state.login.authUser.jwt !== '') {
        console.log('notAuthenticated.js', store.state.login.authUser.jwt)
        return redirect('/')
    }
}
