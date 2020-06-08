<template>
    <div id="login-page">
        <video id="background-video" preload="auto" autoplay="true" loop="loop" muted="muted" volume="0">
            <source src="/login-background.mp4" />
        </video>
        <div id="background-video-overlay"></div>
        <transition name="animate" enter-active-class="animated zoomInDown" leave-active-class="animated zoomOutDonw">
            <div v-if="animate" v-show="loading" id="login-page-modal">
                <div id="login-title">
                    <h2>
                        <span>M</span>
                        <span>E</span>
                        <span>D</span>
                        <span>I</span>
                        <span id="last">A</span>
                        <span>H</span>
                        <span>U</span>
                        <span>B</span>
                    </h2>
                </div>
                <div id="login-button-wrap">
                    <button id="border-animate" @click="login"><img src="../assets/images/google-logo.png" />Login</button>
                    <div class="border"></div>
                    <div class="border"></div>
                    <div class="border"></div>
                    <div class="border"></div>
                </div>
            </div>
        </transition>
    </div>
</template>

<script>
export default {
    data() {
        return {
            loading: false,
            animate: true,
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
            window.location.href = 'https://k02d1031.p.ssafy.io:8081/oauth2/authorize/google?redirect_uri=http://localhost:3000/login'
        },
    },
}
</script>

<style lang="scss" scoped>
#login-page {
    font-family: 'Arita-dotum-Medium';
    position: fixed;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100vw;
    height: 100vh;
    overflow: hidden;
    #background-video {
        position: absolute;
        top: 0px;
        left: 0px;
        width: auto;
        height: auto;
        min-width: 100%;
        min-height: 100%;
        z-index: -3;
        overflow: hidden;
    }
    #background-video-overlay {
        position: absolute;
        top: 0px;
        left: 0px;
        width: auto;
        height: auto;
        min-width: 100%;
        min-height: 100%;
        z-index: -1;
        background-color: rgba(255, 255, 255, 0.55);
        overflow: hidden;
        //background-color: rgba(0, 0, 0, 0.6);
    }
    #login-page-modal {
        position: absolute;
        transition: all 1s;
        width: 390px;
        height: 320px;
        transform: rotate(-7.45deg);
        top: 296px;
        left: 613px;
        border-radius: 27px;
        // width: 80%;
        // min-width: 400px;
        // max-width: 560px;
        // height: 45%;
        // max-height: 338px;
        // min-height: 225px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        background-color: rgba(0, 0, 0, 0);
        //box-shadow: 0px 0px 8px 3px rgba(0, 0, 0, 0.2), 0px 0px 24px 2px rgba(0, 0, 0, 0.2);
        #login-title {
            @import url('https://fonts.googleapis.com/css2?family=Titan+One&display=swap');
            width: 100%;
            height: 40%;
            -webkit-font-smoothing: antialiased;
            display: flex;
            justify-content: center;
            align-items: center;

            h2 {
                height: 100px;
            }

            h2 span {
                position: relative;
                top: 2px;
                display: inline-block;
                animation: bounce 0.3s ease infinite alternate;
                font-family: 'Titan One', cursive;
                font-size: 40px;
                color: rgb(78, 78, 78);
                text-shadow: 0 1px 0 #ccc, 0 2px 0 #ccc, 0 3px 0 #ccc, 0 4px 0 #ccc, 0 5px 0 #ccc, 0 6px 0 transparent, 0 7px 0 transparent,
                    0 8px 0 transparent, 0 9px 0 transparent, 0 10px 10px rgba(0, 0, 0, 0.4);
            }

            h2 span:nth-child(2) {
                animation-delay: 0.1s;
            }
            h2 span:nth-child(3) {
                animation-delay: 0.2s;
            }
            h2 span:nth-child(4) {
                animation-delay: 0.3s;
            }
            h2 span:nth-child(5) {
                animation-delay: 0.4s;
            }
            h2 span:nth-child(6) {
                animation-delay: 0.5s;
            }
            h2 span:nth-child(7) {
                animation-delay: 0.6s;
            }
            h2 span:nth-child(8) {
                animation-delay: 0.7s;
            }

            @keyframes bounce {
                100% {
                    top: -2px;
                    text-shadow: 0 1px 0 #ccc, 0 2px 0 #ccc, 0 3px 0 #ccc, 0 4px 0 #ccc, 0 5px 0 #ccc, 0 6px 0 #ccc, 0 7px 0 #ccc, 0 8px 0 #ccc,
                        0 9px 0 #ccc, 0 50px 25px rgba(0, 0, 0, 0.2);
                }
            }
            #last {
                margin-right: 10px;
            }
        }
        #login-button-wrap {
            position: relative;
            width: 40%;
            height: 64px;

            z-index: 100;
            text-align: center;
            z-index: 100;
            #border-animate,
            #border-animate:hover,
            #border-animate:link,
            #border-animate:active,
            #border-animate:visited {
                width: 100%;
                height: 100%;
                padding: 15px;
                vertical-align: middle;
                color: rgb(24, 24, 24);
                text-decoration: none;
                border-width: 0px;
                background-color: rgba(197, 197, 197, 0.082);
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
                    width: 0.1px;
                    style: solid;
                    color: rgba(78, 78, 78, 0.555);
                    radius: 2px;
                }
                z-index: 1;
                animation-duration: 1s;
                animation-fill-mode: both;
                opacity: 0;
            }
            .border-top {
                top: 0px;
                right: 0px;
                border-right-width: 0.1px;
                animation-name: borderTopToBottom;
                opacity: 1;
            }
            .border-right {
                // bottom: -4px;
                right: 0px;
                border-bottom-width: 0.1px;
                animation-name: borderRightToLeft;
                opacity: 1;
            }
            .border-bottom {
                // bottom: 10px;
                left: 0px;
                border-left-width: 0.1px;
                animation-name: borderBottomToTop;
                opacity: 1;
            }
            .border-left {
                top: 0px;
                left: 0px;
                border-top-width: 0.1px;
                animation-name: borderLeftToRight;
                opacity: 1;
            }
            @keyframes borderTopToBottom {
                from {
                    padding-bottom: 0px;
                }
                to {
                    padding-bottom: 64px;
                }
            }
            @keyframes borderBottomToTop {
                from {
                    top: 64px;
                    padding-top: 0px;
                }
                to {
                    top: 0px;
                    padding-top: 64px;
                }
            }
            @keyframes borderLeftToRight {
                from {
                    padding-right: 0px;
                }
                to {
                    padding-right: calc(100% - 2px);
                }
            }
            @keyframes borderRightToLeft {
                from {
                    padding-left: 0px;
                }
                to {
                    padding-left: calc(100% - 2px);
                }
            }
        }
    }
}
</style>
