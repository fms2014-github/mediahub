<template>
    <div id="live-video">
        <div id="button-wrap">
            <button id="changeVideo" @click="nextSlide"></button>
        </div>
        <div id="live-slider">
            <div id="back-blind" class="back-slide"></div>
            <youtube
                id="youtube-video"
                class="back-slide"
                :player-vars="{ autoplay: 0 }"
                :player-width="720"
                :player-height="405"
                :video-id="play"
                @ready="ready"
            />
            <div id="twitch-video" class="front-slide"></div>
        </div>
    </div>
</template>

<script>
export default {
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
                width: 720,
                height: 405,
                channel: 'silphtv',
            }
            // eslint-disable-next-line no-undef,no-var
            this.twitchPlayer = new Twitch.Player('twitch-video', options)
        },
        ready(e) {
            this.player = e.target
        },
        pause() {
            this.player.pauseVideo()
        },
        nextSlide() {
            const youtubeState = document.getElementById('youtube-video').classList
            const twitchState = document.getElementById('twitch-video').classList
            if (youtubeState[0] === 'back-slide') {
                youtubeState.replace('back-slide', 'front-slide')
            } else {
                youtubeState.replace('front-slide', 'back-slide')
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
    button,
    #live-slider {
        display: inline-block;
    }
    #button-wrap {
        display: inline-block;
        margin: 30px;
    }
    #live-slider {
        position: relative;
        width: calc(#{$video-width} + 50px);
        height: $video-height;
        #youtube-video {
            position: absolute;
            transition: all 0.5s;
        }
        #twitch-video {
            position: absolute;
            transition: all 0.5s;
        }
        #back-blind {
            width: $video-width;
            height: $video-height;
            position: absolute;
            z-index: 2;
            background-color: rgba(0, 0, 0, 0);
        }
    }
    #changeVideo {
        background-color: rgba(0, 0, 0, 0);
        border-color: rgba(0, 0, 0, 0);
        transform: translateY(20%);
    }
    #changeVideo::before {
        display: inline-block;
        background-color: rgb(180, 180, 180);
        content: '';
        padding: 0px;
        width: 8px;
        height: 50px;
        border: {
            width: 1px;
            style: solid;
            color: rgb(180, 180, 180);
            radius: 8px 8px 0 0;
        }
        transform: translateX(100%) translateY(-34px) rotate(40deg);
    }
    #changeVideo::after {
        display: inline-block;
        background-color: rgb(180, 180, 180);
        content: '';
        padding: 0px;
        width: 8px;
        height: 50px;
        border: {
            width: 1px;
            style: solid;
            color: rgb(180, 180, 180);
            radius: 0 0 8px 8px;
        }
        transform: rotate(-40deg);
    }
}

.back-slide {
    top: 0px;
    left: 0px;
    z-index: 1;
}
.front-slide {
    top: 0px;
    left: 30px;
    z-index: 4;
}
</style>
