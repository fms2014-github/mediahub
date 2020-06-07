<template>
    <div id="live-video-slide-bar">
        <div id="slide-container">
            <div class="left move-button">
                <img class="image" src="@/assets/images/arrow_left_icon.png" @click="move(1)" />
            </div>
            <div id="inline-block">
                <div id="loading" class="loading">
                    <div class="vertical-center">
                        <img id="loading-img" class="text-image" src="@/assets/images/loading.png" />
                        <h1 id="loading-text" class="text">구독중인 채널의 라이브 방송을 검색중입니다.</h1>
                    </div>
                </div>
                <div v-if="liveList.length > 0">
                    <div v-for="(item, index) in liveList" :id="'v' + index" :key="item.id" :class="'video v' + index">
                        <nuxt-link class="link" :to="'/streaming/' + item.kind + ',' + item.id"></nuxt-link>
                        <youtube
                            v-if="item.kind === 'google'"
                            :id="'youtube-video' + index"
                            class="back-slide"
                            :player-vars="{ autoplay: 0, rel: 0 }"
                            :player-width="'100%'"
                            :player-height="330"
                            :video-id="item.id"
                            @ready="ready(index, $event)"
                            @playing="playing"
                            @paused="pausing"
                        />
                        <div v-else :id="'twitch-video' + index"></div>
                    </div>
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
            curIdx: 0,
            liveListInfo: new Array(10),
            isPlay: false,
            liveList: [],
        }
    },
    watch: {
        loadingCnt(newValue, oldValue) {
            const N = this.liveList.length
            if (newValue === N) {
                if (N > 4) {
                    this.curIdx = 2
                } else if (N > 1 && N <= 4) {
                    for (let i = 0; i < N; i++) {
                        document.getElementById('v' + i).className = 'video v' + (i + 1)
                    }
                    if (N === 4) {
                        document.getElementById('v3').className = 'video v5'
                    }
                    this.curIdx = 1
                } else if (N === 1) {
                    document.getElementById('v0').className = 'video v2'
                }

                this.loaded()
            }
        },
    },
    async mounted() {
        const labels = JSON.parse(localStorage.getItem('labels'))
        for (const label of labels) {
            for (const item of label.channels) {
                if (item.provider === 'twitch') continue
                const data = await this.$youtubeApi.youtubuLiveVideoApi(item.channelId, item.name)
                if (data.items.length === 0) continue
                this.liveList.push({ kind: 'google', id: data.items[0].id.videoId })
                if (this.liveList.length === 5) break
            }
        }
        const auth = JSON.parse(localStorage.getItem('auth'))
        const i = auth.findIndex((i) => i.provider === 'twitch')
        if (i >= 0) {
            const lives = (await this.$twitchApi.twitchStreamsApi(auth[i].access_token)).data.streams
            for (const item of lives) {
                this.liveList.push({ kind: 'twitch', id: item.channel.name })
                if (this.liveList.length === 10) break
            }
            console.log('lives', lives)
        }
        setTimeout(() => {
            console.log('liveList', this.liveList)
            if (this.liveList.length === 0) {
                this.loaded()
            } else {
                for (let i = 0; i < this.liveList.length; i++) {
                    if (this.liveList[i].kind === 'twitch') {
                        const options = {
                            width: '100%',
                            height: 330,
                            channel: this.liveList[i].id,
                        }
                        // eslint-disable-next-line no-undef
                        const video = new Twitch.Player('twitch-video' + i, options)
                        this.liveListInfo.splice(i, 1, video)
                        // eslint-disable-next-line no-undef
                        this.liveListInfo[i].addEventListener(Twitch.Player.READY, () => {
                            this.liveListInfo[i].pause()
                            this.loadingCnt++
                        })
                    }
                }
            }
        }, 1000)

        window.onkeydown = () => {
            if (event.keyCode !== 32) return
            if (this.liveListInfo[this.curIdx] === 'google') {
                if (this.isPlay) this.liveListInfo[this.curIdx].pauseVideo()
                else this.liveListInfo[this.curIdx].playVideo()
            } else if (this.liveListInfo[this.curIdx].isPaused()) {
                this.liveListInfo[this.curIdx].play()
            } else this.liveListInfo[this.curIdx].pause()
        }
    },
    methods: {
        move(dir) {
            const N = this.liveList.length
            if (this.loadingCnt !== N || N <= 1) return
            const idx = []
            let name
            for (let i = 0; i < N; i++) {
                name = document.getElementById('v' + i).className
                idx.push(Number(name.substring(name.length - 1, name.length)))
            }
            if (dir === 1) idx.push(idx.shift())
            else idx.unshift(idx.pop())

            for (let i = 0; i < N; i++) {
                document.getElementById('v' + i).className = 'change video v' + idx[i]
            }

            this.pause(this.curIdx)
            this.curIdx = this.cal(this.curIdx, -dir, N)
            this.play(this.curIdx)
        },
        pause(idx) {
            if (this.liveList[idx].kind === 'google') {
                this.liveListInfo[idx].stopVideo()
            } else {
                this.liveListInfo[idx].pause()
            }
        },
        play(idx) {
            if (this.liveList[idx].kind === 'google') {
                this.liveListInfo[idx].playVideo()
            } else {
                this.liveListInfo[idx].play()
            }
        },
        cal(idx, dir, N) {
            const next = idx + dir
            if (next < 0) return N - 1
            return next % N
        },
        ready(index, event) {
            this.liveListInfo.splice(index, 1, event.target)
            this.loadingCnt++
        },
        playing(event) {
            this.isPlay = true
        },
        pausing(event) {
            this.isPlay = false
        },
        loaded() {
            if (this.liveList.length !== 0) {
                const loading = document.getElementById('loading')
                loading.style.display = 'none'
                this.play(this.curIdx)
            } else {
                const img = document.getElementById('loading-img')
                img.setAttribute('src', require('~/assets/images/no-live.png'))
                const text = document.getElementById('loading-text')
                text.textContent = '현재 진행중인 라이브 방송이 없습니다.'
            }
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

            width: 80%;
            height: 400px;
            left: 50%;
            transform: translateX(-50%);
            .loading {
                width: 100%;
                background-color: white;
                display: inline-block;
                text-align: center;
                justify-content: middle;
                position: absolute;
                top: 50%;
                transform: translateY(-50%);
                height: 330px;
                box-shadow: 1px 1px 10px black;
                z-index: 10;

                .vertical-center {
                    display: flex;
                    flex-direction: column;
                    justify-content: center;
                    align-items: center;
                    height: 100%;

                    .text-image {
                    }
                    .text {
                        font-size: 32px;
                        font-weight: bold;
                        text-align: center;
                        vertical-align: middle;
                        margin-top: 20px;
                    }
                }
            }

            .link {
                position: absolute;
                display: inline-block;
                height: calc(100% - 45px);
                width: 100%;
                top: 0px;
                background-color: #00ff0000;
                // background-color: yellow;
            }
            .change {
                transition: all 0.2s;
            }
            .video {
                display: inline-block;
                position: absolute;
                top: 50%;
                transform: translateY(-50%);
                overflow: hidden;
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
