<template>
    <div id="upload-video">
        <div id="upload-vcontainer">
            <div id="inline-block">
                <iframe
                    :src="'https://clips.twitch.tv/embed?clip=' + `${playInfo.play}` + '&parent=streamernews.example.com&parent=embed.example.com'"
                    height="500"
                    width="100%"
                    frameborder="0"
                    scrolling="no"
                    preload="metadata"
                    allowfullscreen="true"
                    muted="false"
                    class="video"
                >
                </iframe>
            </div>
        </div>
    </div>
</template>

<script>
export default {
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
        // console.log(await this.$twitchApi.twitchClipsByChannelApi(this.playInfo.channelId))
        // if (this.playInfo.kind === 'twitch') {
        //     const options = {
        //         width: '100%',
        //         height: 500,
        //         video: this.playInfo.play, // 영상,
        //         // clip: 'CoweringFrozenTeaPeanutButterJellyTime',
        //     }
        //     // eslint-disable-next-line no-undef
        //     this.Info = new Twitch.Player('twitch-video', options)
        //     this.Info.addEventListener(Twitch.Player.READY, () => {
        //         this.loaded()
        //     })
        // }
        // window.onkeydown = () => {
        //     if (event.keyCode !== 32) return
        //     if (this.playInfo.kind === 'google') {
        //         if (this.isPlay) this.Info.pauseVideo()
        //         else this.Info.playVideo()
        //     } else if (this.Info.isPaused()) {
        //         this.Info.play()
        //     } else this.Info.pause()
        // }
    },
    methods: {
        ready(event) {
            this.loaded()
            // console.log(event)
        },
        playing(event) {
            this.Info = event.target
            this.isPlay = true
        },
        pause(event) {
            this.isPlay = false
        },
        loaded() {
            const loading = document.getElementById('loading')
            loading.style.display = 'none'
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

            #loading {
                background-color: #e6e6ea;
                display: inline-block;
                position: absolute;
                width: 100%;
                height: 510px;
                top: 50%;
                transform: translateY(-50%);
                box-shadow: 1px 1px 10px black;
                z-index: 10;
            }

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
