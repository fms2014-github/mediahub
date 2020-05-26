<template>
    <div id="upload-video">
        <div id="upload-vcontainer">
            <div id="inline-block">
                <div v-if="playInfo.kind === 'y'">
                    <youtube
                        id="youtube-video"
                        class="back-slide video"
                        :player-vars="{ autoplay: 1, rel: 0, loop: 1, playlist: play }"
                        :player-width="'100%'"
                        :player-height="500"
                        :video-id="playInfo.play"
                        @ready="ready"
                        @playing="playing"
                        @paused="pause"
                    />
                </div>
                <div v-else>
                    <div id="twitch-video" class="video"></div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    // props: { kind: String },
    props: { playInfo: { type: Object, default: null } },
    data: () => {
        return {
            // play: 'fvjpE_wFL5A',
            isPlay: false,
            Info: {},
        }
    },
    created() {},
    mounted() {
        if (this.playInfo.kind === 't') {
            const options = {
                width: '100%',
                height: 500,
                video: this.playInfo.play, // 영상,
                // clip: 'CoweringFrozenTeaPeanutButterJellyTime',
            }

            // eslint-disable-next-line no-undef
            this.Info = new Twitch.Player('twitch-video', options)
        }

        window.onkeydown = () => {
            if (event.keyCode !== 32) return
            if (this.playInfo.kind === 'y') {
                if (this.isPlay) this.Info.pauseVideo()
                else this.Info.playVideo()
            } else if (this.Info.isPaused()) {
                this.Info.play()
            } else this.Info.pause()
        }
    },
    methods: {
        ready(event) {
            // console.log(event)
        },
        playing(event) {
            this.Info = event.target
            this.isPlay = true
        },
        pause(event) {
            this.isPlay = false
        },
    },
}
</script>

<style lang="scss" scoped>
#upload-video {
    #upload-vcontainer {
        position: relative;
        width: 100%;
        height: 600px;

        #inline-block {
            position: absolute;
            display: inline-block;
            width: 100%;
            height: 600px;

            .video {
                position: absolute;
                width: 100%;
                top: 50%;
                transform: translateY(-50%);
            }
        }
    }
}
</style>
