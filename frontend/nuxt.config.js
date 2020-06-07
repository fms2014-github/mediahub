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
            { rel: 'icon', type: 'image/x-icon', href: '/favicon.png' },
            { rel: 'stylesheet', href: 'https://fonts.googleapis.com/icon?family=Material+Icons' },
            { rel: 'stylesheet', type: 'text/css', href: 'https://cdn.jsdelivr.net/npm/animate.css@3.5.1'},
        ],
        script: [
            { src: 'https://player.twitch.tv/js/embed/v1.js' },
            { src: 'https://apis.google.com/js/platform.js' },
            { src: 'https://www.youtube.com/iframe_api' },
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
    plugins: [
        { src: '~/plugins/youtubeEmbed', mode: 'client' },
        '~/plugins/loginAxios.js',
        '~/plugins/youtubeApi.js',
        '~/plugins/twitchApi.js',
        '~/plugins/backendAxios.js',
    ],
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
            cookie: { maxAge: 6000000 },
        }),
        // Api middleware
        // We add /api/login & /api/logout routes
        '~/api',
    ],
}
