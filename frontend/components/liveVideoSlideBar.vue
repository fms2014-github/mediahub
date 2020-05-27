<template>
    <div id="live-video-slide-bar">
        <div id="slide-container">
            <div class="left move-button">
                <img class="image" src="@/assets/images/arrow_left_icon.png" @click="move(1)" />
            </div>
            <div id="inline-block">
                <div v-for="n in 5" :key="n" :class="'loading d' + n"></div>
                <div v-for="(item, index) in plays" :id="'v' + index" :key="item" :class="'video v' + index">
                    <youtube
                        v-if="index % 2 == 0"
                        :id="'youtube-video' + index"
                        class="back-slide"
                        :player-vars="{ autoplay: 0, rel: 0 }"
                        :player-width="'100%'"
                        :player-height="330"
                        :video-id="item"
                        @ready="ready(index, $event)"
                        @playing="playing"
                        @paused="pause"
                    />
                    <div v-else :id="'v' + index"></div>
                </div>
            </div>
            <div class="right move-button">
                <img class="image" src="@/assets/images/arrow_right_icon.png" @click="move(-1)" />
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data: () => {
        return {
            loadingCnt: 0,
            curIdx: 2,
            plays: [
                'fvjpE_wFL5A',
                'BcqxLCWn-CE',
                'Xzuy9C9Y9P4',
                'VBWGwLrC8yY',
                'Mcnu-jrpwS4',
                'Vsj54gbwcbQ',
                'ky7EbHBmwwo',
                'NqwgNCPzCTg',
                'FbxO4R8137Q',
                'Nt8Ec7sS6Oc',
            ],
            twitchs: ['dancingshana', 'ch1ckenkun', 'wpckor', 'tmxk319', 'rosebari', 'silphtv', 'yugyungwoo', 'h0270', 'poongkotv', 'bamgasi87'],
            liveList: new Array(10),
            isPlay: false,
        }
    },
    watch: {
        loadingCnt(newValue, oldValue) {
            if (newValue === 10) {
                const d = document.getElementsByClassName('loading')
                for (let i = 0; i < d.length; i++) {
                    d[i].style.display = 'none'
                }
                this.liveList[this.curIdx].playVideo()
            }
        },
    },
    created() {},
    mounted() {
        for (let i = 1; i < this.twitchs.length; i += 2) {
            const options = {
                width: '100%',
                height: 330,
                channel: this.twitchs[i],
            }
            // eslint-disable-next-line no-undef
            const video = new Twitch.Player('v' + i, options)
            this.liveList.splice(i, 1, video)
            // eslint-disable-next-line no-undef
            this.liveList[i].addEventListener(Twitch.Player.READY, () => {
                this.liveList[i].pause()
                this.loadingCnt++
            })
        }

        window.onkeydown = () => {
            if (event.keyCode !== 32) return
            if (this.curIdx % 2 === 0) {
                if (this.isPlay) this.liveList[this.curIdx].pauseVideo()
                else this.liveList[this.curIdx].playVideo()
            } else if (this.liveList[this.curIdx].isPaused()) {
                this.liveList[this.curIdx].play()
            } else this.liveList[this.curIdx].pause()
        }
    },
    methods: {
        move(dir) {
            if (!this.liveList[this.curIdx]) return
            const videos = document.getElementsByClassName('video')
            const N = videos.length
            let idx
            for (let i = 0; i < N; i++) {
                idx = this.cal(Number(document.getElementById('v' + i).className.substring(7, 8)), dir, N)
                document.getElementById('v' + i).className = 'video v' + idx
            }
            if (this.curIdx % 2 === 0) {
                this.liveList[this.curIdx].stopVideo()
                this.curIdx = this.cal(this.curIdx, -dir, N)
                this.liveList[this.curIdx].play()
            } else {
                this.liveList[this.curIdx].pause()
                this.curIdx = this.cal(this.curIdx, -dir, N)
                this.liveList[this.curIdx].playVideo()
            }
        },
        cal(idx, dir, N) {
            const next = idx + dir
            if (next < 0) return N - 1
            return next % N
        },
        ready(index, event) {
            this.loadingCnt++
            this.liveList.splice(index, 1, event.target)
        },
        playing(event) {
            this.isPlay = true
        },
        pause(event) {
            this.isPlay = false
        },
    },
}
</script>

<style lang="scss" scoped>
#live-video-slide-bar {
    #slide-container {
        background-color: rgb(40, 40, 40);
        position: relative;
        width: 100%;
        height: 400px;

        #inline-block {
            position: absolute;
            display: inline-block;

            // background-color: red;
            width: 80%;
            height: 400px;
            left: 50%;
            transform: translateX(-50%);

            .loading {
                background-color: #e6e6ea;
                display: inline-block;
                position: absolute;
                top: 50%;
                transform: translateY(-50%);
                height: 330;
                box-shadow: 1px 1px 10px black;
            }
            .d1 {
                left: 5%;
                width: 30%;
                height: 210px;
                z-index: 1;
            }
            .d2 {
                left: 12%;
                width: 40%;
                height: 270px;
                z-index: 2;
            }
            .d3 {
                left: 20%;
                width: 60%;
                height: 330px;
                z-index: 3;
            }
            .d4 {
                right: 12%;
                width: 40%;
                height: 270px;
                z-index: 2;
            }
            .d5 {
                right: 5%;
                width: 30%;
                height: 210px;
                z-index: 1;
            }
            .video {
                display: inline-block;
                position: absolute;
                top: 50%;
                transform: translateY(-50%);
                overflow: hidden;
                transition: all 0.2s;
            }
            .v0 {
                left: 5%;
                width: 30%;
                height: 210px;
                z-index: 0;
            }
            .v1 {
                left: 12%;
                width: 40%;
                height: 270px;
                z-index: 1;
            }
            .v2 {
                left: 20%;
                width: 60%;
                height: 330px;
                z-index: 2;
            }
            .v3 {
                right: 12%;
                width: 40%;
                height: 270px;
                z-index: 1;
            }
            .v4 {
                right: 5%;
                width: 30%;
                height: 210px;
                z-index: 0;
            }
            .v5,
            .v6,
            .v7,
            .v8,
            .v9 {
                display: none;
            }
        }
        .move-button {
            position: absolute;
            display: inline-block;
            height: auto;
            top: 50%;
            transform: translateY(-50%);
        }
        .move-button:active {
            box-shadow: 0px 0px 3px 3px rgb(247, 200, 114);
            border-radius: 7px;
        }
        .left {
            left: 5%;
            z-index: 10;
        }
        .right {
            right: 5%;
            z-index: 10;
        }
        .image {
            filter: invert(100%);
        }
    }
}
</style>
