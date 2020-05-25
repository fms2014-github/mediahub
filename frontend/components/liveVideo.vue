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
            })
        },
        ready(e) {
            this.player = e.target
            document.querySelector('#youtube-video iframe').style.width = '100%'
            document.querySelector('#youtube-video iframe').style.height = '100%'
            document.querySelector('#youtube-video iframe').style.position = 'absolute'
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
    align-items: center;
    justify-content: center;
    margin-top: 0px;
    height: 100vh;
    max-height: 600px;
    min-height: 540px;
    min-width: 1070px;
    #live-chat {
        float: right;
    }
    button,
    #button-wrap {
        margin: 30px 30px 30px 0;
    }
    #live-slider {
        position: relative;
        width: calc(100% - 180px);
        height: 100%;
        margin-right: 40px;
        max-width: 960px;
        max-height: 540px;
        min-width: 640px;
        min-height: 360px;
        float: left;
        #youtube-video {
            width: 100%;
            height: 0;
            padding-bottom: 56.25%;
            position: absolute;
            transition: all 0.5s;
        }
        #twitch-video {
            width: 100%;
            height: 0;
            padding-bottom: 56.25%;
            position: absolute;
            transition: all 0.5s;
        }
        #back-blind {
            position: absolute;
            width: 100%;
            height: 100%;
            z-index: 2;
            background-color: rgba(0, 0, 0, 0);
        }
    }
    #changeVideo {
        width: 60px;
        height: 90px;
        padding: 0px;
        margin: 0px;
        vertical-align: top;
        background-color: rgba(0, 0, 0, 0);
        border-color: rgba(0, 0, 0, 0);
        transform: translateY(20%);
    }
    #changeVideo::before {
        display: inline-block;
        vertical-align: middle;
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
        vertical-align: middle;
        position: absolute;
        bottom: 0px;
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
    top: 50%;
    transform: translateY(-50%);
    left: 0px;
    z-index: 1;
}
.front-slide {
    top: 50%;
    transform: translateY(-50%);
    left: 30px;
    z-index: 4;
}
</style>
