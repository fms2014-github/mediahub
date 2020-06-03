export const actions = {
    // nuxtServerInit is called by Nuxt.js before server-rendering every page
    async nuxtServerInit({ commit }, { req }) {
        if (req.session && req.session.jwt) {
            try {
                const data = (await this.$test.get('getTest')).data
                console.log(data)
            } catch (error) {}
            console.log('asdf', req.session.jwt)
            commit('login/SET_USER', req.session.jwt)
        }
    },
}
