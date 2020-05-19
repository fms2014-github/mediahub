<template>
    <div id="live-video">
        <button @click="nextSlide">버튼</button>
        <div id="live-slider">
            <youtube
                id="youtube-video"
                class="back-slide"
                :player-vars="{ autoplay: 0 }"
                :player-width="880"
                :player-height="500"
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
                width: 880,
                height: 500,
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
#live-video {
    button,
    #live-slider {
        display: inline-block;
    }
    #live-slider {
        position: relative;
        width: 920px;
        height: 500px;
        #youtube-video {
            position: absolute;
            transition: all 0.5s;
        }
        #twitch-video {
            position: absolute;
            transition: all 0.5s;
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
    }
}
</style>
