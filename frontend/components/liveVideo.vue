<template>
    <div id="live-video">
        <div id="live-slider">
            <div id="button-wrap">
                <button id="changeVideo" @click="nextSlide"></button>
            </div>
            <div id="video-wrap">
                <div id="back-blind" class="back-slide"></div>
                <youtube id="youtube-video" class="back-slide" :player-vars="{ autoplay: 0 }" :video-id="play" @ready="ready" />
                <div id="twitch-video" class="front-slide"></div>
                <live-chat></live-chat>
            </div>
        </div>
    </div>
</template>

<script>
import liveChat from '@/components/livechat.vue'
export default {
    components: {
        liveChat,
    },
    data() {
        return {
            play: 'P6blDnXcXaY',
            videoEvent: {
                paused: 0,
            },
        }
    },
    mounted() {
        this.init()
    },
    methods: {
        init() {
            const options = {
                width: 0,
                height: 0,
                channel: 'silphtv',
            }
            // eslint-disable-next-line no-undef,no-var
            this.twitchPlayer = new Twitch.Player('twitch-video', options)
            // eslint-disable-next-line no-undef
            this.twitchPlayer.addEventListener(Twitch.Player.READY, () => {
                document.querySelector('#twitch-video iframe').style.width = '100%'
                document.querySelector('#twitch-video iframe').style.height = '100%'
                // document.querySelector('#twitch-video iframe').style.position = 'absolute'
                // document.querySelector('#twitch-video iframe').style.top = '0px'
                // document.querySelector('#twitch-video iframe').style.left = '0px'
            })
        },
        ready(e) {
            this.player = e.target
            document.querySelector('#youtube-video iframe').style.width = '100%'
            document.querySelector('#youtube-video iframe').style.height = '100%'
            // document.querySelector('#youtube-video iframe').style.position = 'absolute'
            // document.querySelector('#youtube-video iframe').style.top = '0px'
            // document.querySelector('#youtube-video iframe').style.left = '0px'
        },
        youtubePlay() {
            this.player.playVideo()
        },
        youtubePause() {
            this.player.pauseVideo()
        },
        nextSlide() {
            const youtubeState = document.getElementById('youtube-video').classList
            const twitchState = document.getElementById('twitch-video').classList
            if (youtubeState[0] === 'back-slide') {
                youtubeState.replace('back-slide', 'front-slide')
                this.youtubePlay()
            } else {
                youtubeState.replace('front-slide', 'back-slide')
                this.youtubePause()
            }
            if (twitchState[0] === 'back-slide') {
                twitchState.replace('back-slide', 'front-slide')
                this.twitchPlayer.play()
            } else {
                twitchState.replace('front-slide', 'back-slide')
                this.twitchPlayer.pause()
            }
        },
    },
}
</script>

<style lang="scss" scoped>
@import '~/assets/commonMixin';
#live-video {
    height: 100%;
    #live-slider {
        display: flex;
        padding-right: 50px;
        flex-basis: 100%;
        #button-wrap {
            margin: 0 20px;
            #changeVideo {
                display: inline-block;
                position: relative;
                width: 60px;
                height: 90px;
                top: 50%;
                transform: translateY(-50%);
                padding: 0px;
                margin: 0px;
                background-color: rgba(0, 0, 0, 0);
                border-color: rgba(0, 0, 0, 0);
            }
            #changeVideo::before {
                display: inline-block;
                position: absolute;
                top: 0px;
                background-color: rgb(180, 180, 180);
                content: '';
                padding: 0px;
                width: 8px;
                height: 50px;
                border: {
                    width: 1px;
                    style: solid;
                    color: rgb(180, 180, 180);
                    radius: 8px 8px 0 8px;
                }
                transform: rotate(40deg);
            }
            #changeVideo::after {
                display: inline-block;
                position: absolute;
                top: 34px;
                background-color: rgb(180, 180, 180);
                content: '';
                padding: 0px;
                width: 8px;
                height: 50px;
                border: {
                    width: 1px;
                    style: solid;
                    color: rgb(180, 180, 180);
                    radius: 8px 0 8px 8px;
                }
                transform: rotate(-40deg);
            }
        }
        #video-wrap {
            position: relative;
            width: 100%;
            height: 0;
            padding-bottom: calc(56.25% - 56.25px);
            #youtube-video {
                position: absolute;
                width: 100%;
                height: 100%;
                transition: all 0.5s;
            }
            #twitch-video {
                position: absolute;
                width: 100%;
                height: 100%;
                transition: all 0.5s;
            }
            #back-blind {
                position: absolute;
                width: 100%;
                height: 100%;
                z-index: 2;
                background-color: rgba(0, 0, 0, 0);
            }
            #live-chat {
                position: absolute;
                margin-left: calc(100% + 50px);
                height: calc(100% - 2px);
            }
        }
    }
}

.back-slide {
    transform: scale(0.9) translateX(-5%);
    left: 0px;
    z-index: 1;
}
.front-slide {
    left: 50px;
    z-index: 4;
}
</style>
