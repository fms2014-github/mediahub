<template>
    <div id="button">
        <div id="button-container">
            <div v-if="playInfo.kind === 'google'">
                <div class="flex-container">
                    <div v-if="!isSubscribe" class="youtube-red button" @click="youtubeInsert">구독</div>
                    <divs v-else class="youtube-gray button" @click="youtubeDelete">구독중</divs>
                </div>
            </div>
            <div v-else>
                <div class="flex-container">
                    <div class="twitch button">팔로우</div>
                    <div class="twitch button">구독</div>
                    <div class="twitch button">후원</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
    props: { playInfo: { type: Object, default: null } },
    data: () => {
        return {
            isSubscribe: false,
            youtube: {
                subscribeId: '',
            },
            labels: null,
        }
    },
    created() {
        // back에서 구독리스트 받아오면 그거 hashmap해서 구독 여부 확인 -> sideBar로 빠지고 local로 찾기
    },
    async mounted() {
        this.labels = (
            await this.$axios.get('https://k02d1031.p.ssafy.io:8081/v1/member/information', {
                headers: { Authorization: 'Bearer ' + this.getJwt() },
            })
        ).data.label
        const data = this.labels
        for (const d of data) {
            console.log(d)
            console.log()
            const i = d.channels.findIndex((i) => i.channelId === this.playInfo.channelId && i.provider === this.playInfo.kind)
            if (i >= 0) {
                this.isSubscribe = true
                this.youtube.subscribeId = d.channels[i].id
            }
            console.log(i)
        }
        // console.log(JSON.parse(localStorage.getItem('labels')))
        // const data = JSON.parse(localStorage.getItem('labels'))
        // console.log(data)
        // var index = data[0].channels.findIndex((i) => i.channelId === this.playInfo.channelId && i.provider === this.playInfo.kind)
        // console.log(index)
        // console.log(data[0].channels[index])
    },
    methods: {
        ...mapGetters({ getJwt: 'login/getJwt' }),
        async youtubeInsert() {
            // 백에서 받아서 localStorage에 저장 or 카테고리 동기화
            const res = await this.$backendAxios.insertChannel(this.playInfo.channelId)
            console.log(res)
            this.isSubscribe = true
            // this.youtube.subscribeId =
        },
        youtubeDelete() {
            // this.$youtubeApi.deleteSubscribeApi(this.subscribeInfo.subscribeId)
            this.$backendAxios.deleteChannel(this.youtube.subscribeId)
            this.isSubscribe = false
            this.youtube.subscribeId = ''
        },
    },
}
</script>

<style lang="scss" scoped>
#button {
    #button-container {
        // background-color: rgb(40, 40, 40);
        display: inline-block;
        width: 100%;
        height: 60px;

        .flex-container {
            display: inline-flex;
            width: 100%;
            justify-content: flex-end;

            .button {
                margin-left: 2%;
                width: 8%;
                height: 35px;
                text-align: center;
                line-height: 35px;
                font-weight: bold;
                cursor: pointer;
                font-size: 1rem;
            }
            .youtube-white {
                background-color: white;
                border: 1px solid black;
            }
            .youtube-red {
                background-color: #e24821;
                border: 1px solid red;
                color: white;
            }
            .youtube-gray {
                background-color: #ddd;
                border: 1px solid #ddd;
                color: gray;
            }
            .twitch {
                background-color: #d6c7ff;
                border: 1px solid #d6c7ff;
            }
        }
    }
}
</style>
