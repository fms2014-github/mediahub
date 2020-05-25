<template>
    <div id="upload-video">
        <div id="upload-vcontainer">
            <div id="inline-block">
                <div v-if="kind === 'y'">
                    <youtube
                        id="youtube-video"
                        class="back-slide video"
                        :player-vars="{ autoplay: 1, rel: 0, loop: 1, playlist: play }"
                        :player-width="'100%'"
                        :player-height="500"
                        :video-id="play"
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
    data: () => {
        return {
            kind: 'y',
            play: 'fvjpE_wFL5A',
            isPlay: false,
        }
    },
    created() {},
    mounted() {
        if (this.kind === 't') {
            const options = {
                width: '100%',
                height: 500,
                video: '624698735', // 영상,
                Info: {},
                // clip: 'CoweringFrozenTeaPeanutButterJellyTime',
            }

            // eslint-disable-next-line no-undef
            this.Info = new Twitch.Player('twitch-video', options)
        }

        window.onkeydown = () => {
            if (event.keyCode !== 32) return
            if (this.kind === 'y') {
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
        // background-color: rgb(40, 40, 40);
        position: relative;
        width: 100%;
        height: 600px;

        #inline-block {
            position: absolute;
            display: inline-block;

            // background-color: red;
            width: 70%;
            height: 600px;
            left: 50%;
            transform: translateX(-50%);

            .video {
                position: absolute;
                width: 100%;
                top: 50%;
                transform: translateY(-50%);
            }
            .flex-container {
                display: inline-flex;
                width: 100%;
                justify-content: flex-end;
            }
            .button {
                // position: absolute;
                margin-left: 5px;
                width: 8%;
                height: 35px;
                bottom: 0px;
                background-color: #d6c7ff;
            }
        }
    }
}
</style>
