<template>
    <div id="login-page">
        <div v-show="loading" id="login-page-modal">
            <h1>동영상 통합 관리 서비스</h1>
            <h2>Social Login</h2>
            <div id="login-button-wrap">
                <button id="border-animate" @click="login"><img src="../assets/images/google-logo.png" />Login with Google</button>
                <div class="border"></div>
                <div class="border"></div>
                <div class="border"></div>
                <div class="border"></div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            loading: false,
        }
    },
    layout: 'cover',
    middleware: 'notAuthenticated',
    async mounted() {
        const fragmentString = window.location.search.replace('?', '')
        // Parse query string to see if page request is coming from OAuth 2.0 server.
        const params = {}
        const regex = /([^&=]+)=([^&]*)/g
        let m
        while ((m = regex.exec(fragmentString))) {
            if (decodeURIComponent(m[1]) === 'token') {
                console.log('awefwaef')
                await this.$store.dispatch('login/loginapi', decodeURIComponent(m[2]))
                this.$router.push('/subSync')
            }
        }
        setTimeout(() => {
            this.loading = true
            const className = ['border-top', 'border-right', 'border-bottom', 'border-left']
            const getElement = document.getElementById('border-animate')
            getElement.addEventListener('mouseover', () => {
                for (let i = 0; i < 4; i++) {
                    document.getElementsByClassName('border')[i].classList.add(className[i])
                }
            })
            getElement.addEventListener('mouseleave', () => {
                for (let i = 0; i < 4; i++) {
                    document.getElementsByClassName('border')[i].classList.remove(className[i])
                }
            })
        }, 400)
    },
    methods: {
        login() {
            window.location.href = 'http://k02d1031.p.ssafy.io:8081/oauth2/authorize/google?redirect_uri=' + location.origin + '/login'
        },
    },
}
</script>

<style lang="scss" scoped>
#login-page {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100vw;
    height: 100vh;

    #login-page-modal {
        transition: all 1s;
        width: 80%;
        max-width: 800px;
        height: 45%;
        max-height: 480px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        box-shadow: 0px 0px 8px 3px rgba(0, 0, 0, 0.2), 0px 0px 24px 2px rgba(0, 0, 0, 0.2);
        h1 {
            margin-bottom: 3%;
            font: {
                size: 2rem;
                weight: 600;
            }
        }
        h2 {
            margin-bottom: 6%;
            font: {
                size: 1.5rem;
                weight: 400;
            }
        }
        #login-button-wrap {
            position: relative;
            width: 70%;
            z-index: 100;

            text-align: center;
            border: {
                width: 1px;
                style: solid;
                color: rgb(180, 180, 180);
            }
            z-index: 100;
            #border-animate,
            #border-animate:hover,
            #border-animate:link,
            #border-animate:active,
            #border-animate:visited {
                width: 100%;
                height: 100%;
                padding: 20px;
                vertical-align: middle;
                color: black;
                text-decoration: none;
                border-width: 0px;
                background-color: white;
                img {
                    width: 32px;
                    height: 32px;
                    vertical-align: middle;
                    padding-right: 10px;
                }
            }
            #border-animate:active {
                background-color: rgb(230, 230, 230);
            }
            .border {
                position: absolute;
                border: {
                    width: 2px;
                    style: solid;
                    color: rgb(0, 0, 0);
                    radius: 2px;
                }
                z-index: 1;
                animation-duration: 1s;
                animation-fill-mode: both;
                opacity: 0;
            }
            .border-top {
                top: -4px;
                right: -4px;
                border-right-width: 2px;
                animation-name: borderTopToBottom;
                opacity: 1;
            }
            .border-right {
                bottom: -4px;
                right: -4px;
                border-bottom-width: 2px;
                animation-name: borderRightToLeft;
                opacity: 1;
            }
            .border-bottom {
                bottom: 0px;
                left: -4px;
                border-left-width: 2px;
                animation-name: borderBottomToTop;
                opacity: 1;
            }
            .border-left {
                top: -4px;
                left: -4px;
                border-top-width: 2px;
                animation-name: borderLeftToRight;
                opacity: 1;
            }
            @keyframes borderTopToBottom {
                from {
                    padding-bottom: 0px;
                }
                to {
                    padding-bottom: 74px;
                }
            }
            @keyframes borderBottomToTop {
                from {
                    top: 73px;
                    padding-top: 0px;
                }
                to {
                    top: -2px;
                    padding-top: 74px;
                }
            }
            @keyframes borderLeftToRight {
                from {
                    padding-right: 0px;
                }
                to {
                    padding-right: calc(100% + 2px);
                }
            }
            @keyframes borderRightToLeft {
                from {
                    padding-left: 0px;
                }
                to {
                    padding-left: calc(100% + 2px);
                }
            }
        }
    }
}
</style>
