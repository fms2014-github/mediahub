export const actions = {
    // nuxtServerInit is called by Nuxt.js before server-rendering every page
    nuxtServerInit({ commit }, { req }) {
        if (req.session && req.session.data) {
            commit('login/SET_USER', req.session.data)
        }
    },
}
