<template>
    <div id="button">
        <div id="button-container">
            <div v-if="playInfo.kind === 'youtube'">
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
export default {
    props: { playInfo: { type: Object, default: null } },
    data: () => {
        return {
            isSubscribe: false,
            subscribeId: '',
            labelId: 0,
        }
    },
    async created() {
        if (this.playInfo.kind === 'youtube') {
            const data = (await this.$youtubeApi.isSubscribeApi(this.playInfo.channelId)).data
            if (data.items.length === 1) {
                this.isSubscribe = true
                this.subscribeId = data.items[0].id
            }
        }
        // back에서 구독리스트 받아오면 그거 hashmap해서 구독 여부 확인
    },
    mounted() {},
    methods: {
        async youtubeInsert() {
            const params = {
                channelId: this.playInfo.channelId,
                provider: this.playInfo.kind,
            }
            // 백에서 받아서 localStorage에 저장
            const res = (await this.$backendAxios.insertChannel(params)).data
            console.log(res)
            console.log(JSON.parse(String(res)))
            // res 데이터의 id를 받아서 labelId에 넣어야 삭제가능
            this.$youtubeApi.insertSubscribeApi(this.playInfo.channelId)
            this.isSubscribe = true
            // 구독id 백에서 구독id도 넘겨줘야함
            this.subscribeId = data.id
        },
        youtubeDelete() {
            this.$youtubeApi.deleteSubscribeApi(this.subscribeInfo.subscribeId)
            this.$backendAxios.deleteChannel(this.subscribeInfo.labelId)
            this.isSubscribe = false
            this.subscribeId = ''
            this.labelId = 0
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
