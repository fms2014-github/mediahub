import bodyParser from 'body-parser'
import session from 'express-session'

export default {
    mode: 'universal',
    /*
     ** Headers of the page
     */
    head: {
        title: process.env.npm_package_name || '',
        meta: [
            { charset: 'utf-8' },
            { name: 'viewport', content: 'width=device-width, initial-scale=1' },
            {
                hid: 'description',
                name: 'description',
                content: process.env.npm_package_description || '',
            },
        ],
        link: [
            { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' },
            { rel: 'stylesheet', href: 'https://fonts.googleapis.com/icon?family=Material+Icons' },
        ],
        script: [
            { src: 'https://player.twitch.tv/js/embed/v1.js' },
            { type: 'module', src: 'https://unpkg.com/x-frame-bypass' },
            { src: 'https://unpkg.com/@ungap/custom-elements-builtin' },
        ],
    },
    /*
     ** Customize the progress-bar color
     */
    loading: { color: '#000' },
    /*
     ** Global CSS
     */
    css: ['~/assets/resetCSS.css', { src: '~/assets/commonMixin.scss', lang: 'scss' }],
    /*
     ** Plugins to load before mounting the App
     */
    plugins: [{ src: '~/plugins/youtubeEmbed', mode: 'client' }, '~/plugins/loginAxios.js', '~/plugins/testaxios.js', '~/plugins/youtubeApi.js'],
    /*
     ** Nuxt.js dev-modules
     */
    buildModules: [
        // Doc: https://github.com/nuxt-community/eslint-module
        '@nuxtjs/eslint-module',
    ],
    /*
     ** Nuxt.js modules
     */
    modules: [
        // Doc: https://axios.nuxtjs.org/usage
        '@nuxtjs/axios',
        '@nuxtjs/svg',
    ],
    /*
     ** Axios module configuration
     ** See https://axios.nuxtjs.org/options
     */
    axios: {},
    /*
     ** Build configuration
     */
    build: {
        /*
         ** You can extend webpack config here
         */
        extend(config, ctx) {},
    },
    serverMiddleware: [
        // body-parser middleware
        bodyParser.json(),
        // session middleware
        session({
            secret: 'super-secret-key',
            resave: false,
            saveUninitialized: false,
            cookie: { maxAge: 600000 },
        }),
        // Api middleware
        // We add /api/login & /api/logout routes
        '~/api',
    ],
}
