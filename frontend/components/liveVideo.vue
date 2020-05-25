<template>
    <div id="live-video">
        <div id="button-wrap">
            <button id="changeVideo" @click="nextSlide"></button>
        </div>
        <div id="live-slider">
            <div id="back-blind" class="back-slide"></div>
            <youtube id="youtube-video" class="back-slide" :player-vars="{ autoplay: 0 }" :video-id="play" @ready="ready" />
            <div id="twitch-video" class="front-slide"></div>
        </div>
        <live-chat />
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
                channel: 'silphtv',
            }
            // eslint-disable-next-line no-undef,no-var
            this.twitchPlayer = new Twitch.Player('twitch-video', options)
            // eslint-disable-next-line no-undef
            this.twitchPlayer.addEventListener(Twitch.Player.READY, () => {
                document.querySelector('#twitch-video iframe').style.width = '100%'
                document.querySelector('#twitch-video iframe').style.height = '100%'
                document.querySelector('#twitch-video iframe').style.position = 'absolute'
                document.querySelector('#twitch-video iframe').style.top = '0px'
                document.querySelector('#twitch-video iframe').style.left = '0px'
            })
        },
        ready(e) {
            this.player = e.target
            document.querySelector('#youtube-video iframe').style.width = '100%'
            document.querySelector('#youtube-video iframe').style.height = '100%'
            document.querySelector('#youtube-video iframe').style.position = 'absolute'
            document.querySelector('#youtube-video iframe').style.top = '0px'
            document.querySelector('#youtube-video iframe').style.left = '0px'
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
    display: flex;
    justify-content: center;
    align-items: center;
    button,
    #button-wrap {
        align-self: center;
        margin: 20px;
    }
    #live-slider {
        position: relative;
        align-self: center;
        width: 100%;
        top: 0px;
        left: 0px;
        bottom: 0px;
        height: 100%;
        margin-right: 50px;
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
            width: 90%;
            height: 90%;
            z-index: 2;
            background-color: rgba(0, 0, 0, 0);
        }
    }
    #changeVideo {
        display: inline-block;
        position: relative;
        width: 60px;
        height: 90px;
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

.back-slide {
    width: 90% !important;
    height: 90% !important;
    transform: translateY(5%);
    left: 0px;
    z-index: 1;
}
.front-slide {
    left: 50px;
    z-index: 4;
}
</style>
