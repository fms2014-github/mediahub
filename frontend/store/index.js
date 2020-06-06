export const actions = {
    // nuxtServerInit is called by Nuxt.js before server-rendering every page
    async nuxtServerInit({ commit }, { req }) {
        if (req.session && req.session.data) {
            try {
                const data = (await this.$test.get('getTest')).data
            } catch (error) {}
            commit('login/SET_USER', req.session.data)
        }
    },
}
