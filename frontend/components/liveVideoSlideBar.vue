<template>
    <div id="live-video-slide-bar">
        <div id="slide-container">
            <div class="left move-button">
                <img class="image" src="@/assets/images/arrow_left_icon.png" @click="move(1)" />
            </div>
            <div id="inline-block">
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
            twitchs: ['dancingshana', 'ch1ckenkun', 'wpckor', 'tmxk319', 'rosebari', 'silphtv', 'yugyungwoo', 'bamgasi87', 'poongkotv', 'dyohb'],
            liveList: new Array(10),
            isPlay: false,
        }
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
            this.liveList.splice(index, 1, event.target)
            if (index === 2) {
                event.target.playVideo()
            }
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

            .video {
                display: inline-block;
                position: absolute;
                top: 50%;
                transform: translateY(-50%);
                // border: 1px solid black;
                overflow: hidden;
                transition: all 0.2s;
            }
            .v0 {
                left: 5%;
                width: 30%;
                height: 210px;
            }
            .v1 {
                left: 12%;
                z-index: 1;
                width: 40%;
                height: 270px;
            }
            .v2 {
                left: 20%;
                width: 60%;
                height: 330px;
                z-index: 2;
            }
            .v3 {
                right: 12%;
                z-index: 1;
                width: 40%;
                height: 270px;
            }
            .v4 {
                right: 5%;
                z-index: 0;
                width: 30%;
                height: 210px;
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
