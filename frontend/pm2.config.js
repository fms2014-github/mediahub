const { join } = require('path')
module.exports = {
    apps: [
        {
            name: 'webapp',
            script: './node_modules/nuxt/bin/nuxt.js',
            cwd: './',
            args: `-c ${join(__dirname, 'nuxt.config.js')}`,
            env: {
                HOST: '0.0.0.0',
                PORT: 8080,
            },
            autorestart: true,
            max_memory_restart: '2G',
            wait_ready: true,
            listen_timeout: 20000,
            kill_timeout: 5000,
        },
    ],
}
