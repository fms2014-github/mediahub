<template>
    <div id="button">
        <div id="button-container">
            <div v-if="playInfo.kind === 'youtube'">
                <div class="flex-container">
                    <div
                        v-if="!subscribeInfo.isSubscribe"
                        class="youtube-red button"
                        @click="insertSubscribe"
                    >구독</div>
                    <divs v-else class="youtube-gray button" @click="deleteSubscribe">구독중</divs>
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
            subscribeInfo: {
                isSubscribe: false,
                subscribeId: '',
            },
        }
    },
    async created() {
        const data = (await this.$youtubeTokenApi.isSubscribeApi(this.playInfo.channelId)).data
        if (data.items.length === 1) {
            this.subscribeInfo.isSubscribe = true
            this.subscribeInfo.subscribeId = data.items[0].id
        }
    },
    mounted() {},
    methods: {
        async insertSubscribe() {
            const data = (await this.$youtubeTokenApi.insertSubscribeApi(this.playInfo.channelId)).data
            this.subscribeInfo.isSubscribe = true
            this.subscribeInfo.subscribeId = data.id
        },
        async deleteSubscribe() {
            const data = (await this.$youtubeTokenApi.deleteSubscribeApi(this.subscribeInfo.subscribeId)).data
            this.subscribeInfo.isSubscribe = false
            this.subscribeInfo.subscribeId = ''
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
