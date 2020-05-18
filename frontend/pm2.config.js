const { join } = require('path')
module.exports = {
    apps: [
        {
            name: 'webapp',
            exec_mode: 'cluster',
            instances: 0,
            script: './node_modules/nuxt/bin/nuxt.js',
            cwd: './',
            args: `-c ${join(__dirname, 'nuxt.config.js')}`,
            env: {
                HOST: '0.0.0.0',
                PORT: 8080
            },
            autorestart: true,
            max_memory_restart: '1G',
            wait_ready: true,
            listen_timeout: 10000,
            kill_timeout: 5000
        }
    ]
}
