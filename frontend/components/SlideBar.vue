<template>
    <div id="slide-bar">
        <div id="slide-container">
            <div class="left move-button">
                <img src="@/assets/images/arrow_left_icon.png" @click="move(1)" />
            </div>
            <div id="inline-block">
                <div v-for="(item, index) in plays" :id="'v' + index" :key="item" :class="'video v' + index">
                    <youtube
                        v-if="index % 2 == 0"
                        :id="'youtube-video' + index"
                        class="back-slide"
                        :player-vars="{ autoplay: 0 }"
                        :player-width="'100%'"
                        :player-height="330"
                        :video-id="item"
                        @ready="ready(index, $event)"
                    />
                    <div v-else :id="'v' + index"></div>
                </div>
            </div>
            <div class="right move-button">
                <img src="@/assets/images/arrow_right_icon.png" @click="move(-1)" />
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
                'pPAAFMtDUzo',
                'VBWGwLrC8yY',
                'dXq1JDKudgM',
                'Vsj54gbwcbQ',
                'Rf_xmNMuuPQ',
                'NqwgNCPzCTg',
                'gIowzVds_tE',
                'Nt8Ec7sS6Oc',
            ],
            twitchs: ['dancingshana', 'carpe', 'wpckor', 'valorantkorea', 'rosebari', 'hwiba_h', 'yugyungwoo', 'dyohb', 'poongkotv', 'jegalyangtv'],
            liveList: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        }
    },
    created() {},
    mounted() {
        for (let i = 1; i < this.twitchs.length; i += 2) {
            const options = {
                width: 750,
                height: 330,
                channel: this.twitchs[i],
            }
            // eslint-disable-next-line no-undef
            const video = new Twitch.Player('v' + i, options)
            this.liveList.splice(i, 1, video)
            // eslint-disable-next-line no-undef
            video.addEventListener(Twitch.Player.READY, () => {
                video.pause()
            })
        }
    },
    methods: {
        move(dir) {
            const videos = document.getElementsByClassName('video')
            const N = videos.length
            let temp
            for (let i = 0; i < N; i++) {
                temp = Number(document.getElementById('v' + i).className.substring(7, 8)) + dir
                if (temp < 0) temp = N - 1
                document.getElementById('v' + i).className = 'video v' + (temp % N)
            }
            const d = dir * -1
            if (this.curIdx % 2 === 0) {
                this.liveList[this.curIdx].stopVideo()
                if (this.curIdx + d < 0) this.curIdx = N - 1
                else this.curIdx = (this.curIdx + d) % N
                this.liveList[this.curIdx].play()
            } else {
                this.liveList[this.curIdx].pause()
                if (this.curIdx + d < 0) this.curIdx = N - 1
                else this.curIdx = (this.curIdx + d) % N
                this.liveList[this.curIdx].playVideo()
            }
        },
        ready(index, event) {
            this.liveList.splice(index, 1, event.target)
            if (index === 2) event.target.playVideo()
        },
    },
}
</script>

<style lang="scss" scoped>
#slide-bar {
    // padding-left: 10%;

    #slide-container {
        background-color: aqua;
        position: relative;
        width: 100%;
        height: 400px;

        #inline-block {
            position: absolute;
            display: inline-block;

            background-color: red;
            vertical-align: middle;
            width: 80%;
            height: 400px;
            padding: 20px;
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
            /* width: 5%; */
            height: auto;
            top: 50%;
        }
        .left {
            left: 5%;
            z-index: 10;
        }
        .right {
            right: 5%;
            z-index: 10;
        }
    }
}
</style>
