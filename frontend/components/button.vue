<template>
    <div id="button">
        <div id="button-container">
            <div v-if="playInfo.kind === 'google'">
                <div class="flex-container">
                    <div v-if="!isSubscribe" class="youtube-red button" @click="youtubeInsert">구독</div>
                    <divs v-else class="gray button" @click="youtubeDelete">구독중</divs>
                </div>
            </div>
            <div v-else>
                <div class="flex-container">
                    <div v-if="!isSubscribe" class="twitch button" @click="twitchInsert">팔로우</div>
                    <div v-else class="gray button" @click="twitchDelete">팔로잉</div>
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
            subscribeId: 0,
            twitch: {
                accessToken: '',
                userId: '',
                rootLabelId: '',
            },
            data: null,
            labels: null,
        }
    },
    created() {},
    mounted() {
        this.auth = JSON.parse(localStorage.getItem('auth'))
        this.labels = JSON.parse(localStorage.getItem('labels'))
        if (this.playInfo.kind === 'twitch') {
            const i = this.auth.findIndex((i) => i.provider === 'twitch')
            this.twitch.accessToken = this.auth[i].access_token
            this.twitch.userId = this.auth[i].userId
        }
        this.twitch.rootLabelId = this.labels[0].id
        console.log(this.labels)
        for (const d of this.labels) {
            const i = d.channels.findIndex((i) => i.channelId === this.playInfo.channelId && i.provider === this.playInfo.kind)
            if (i >= 0) {
                this.isSubscribe = true
                this.subscribeId = d.channels[i].id
                break
            }
        }
    },
    methods: {
        async youtubeInsert() {
            const res = await this.$backendAxios.insertYoutubeChannel(this.playInfo.channelId)
            this.insertInit(res)
        },
        youtubeDelete() {
            this.$backendAxios.deleteYoutubeChannel(this.subscribeId)
            this.deleteInit()
        },
        async twitchInsert() {
            const params = {
                channelId: this.playInfo.channelId,
                accessToken: this.twitch.accessToken,
                userId: this.twitch.userId,
                rootLabelId: this.twitch.rootLabelId,
            }
            const res = await this.$backendAxios.insertTwitchChannel(params)
            this.insertInit(res)
        },
        twitchDelete() {
            const params = {
                channelId: this.playInfo.channelId,
                accessToken: this.twitch.accessToken,
                userId: this.twitch.userId,
                channelPk: this.subscribeId,
            }
            this.$backendAxios.deleteTwitchChannel(params)
            this.deleteInit()
        },
        insertInit(res) {
            this.isSubscribe = true
            this.subscribeId = res.data.id
            this.labels[0].channels.push(res.data)
            localStorage.setItem('labels', JSON.stringify(this.labels))
        },
        deleteInit() {
            this.isSubscribe = false
            this.subscribeId = 0
            for (const idx in this.labels) {
                const i = this.labels[idx].channels.findIndex((i) => i.channelId === this.playInfo.channelId && i.provider === this.playInfo.kind)
                if (i >= 0) {
                    this.labels[idx].channels.splice(i, 1)
                    break
                }
            }
            localStorage.setItem('labels', JSON.stringify(this.labels))
        },
    },
}
</script>

<style lang="scss" scoped>
#button {
    font-family: 'S-CoreDream-4Regular';
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
                width: 80px;
                height: 35px;
                text-align: center;
                line-height: 35px;
                cursor: pointer;
                border-radius: 2px;
                font-size: 13px;
                color: white;
                box-shadow: 0 1px 2px rgb(97, 97, 97);
            }
            .youtube-red {
                background-color: red;
            }
            .youtube-red:hover {
                background-color: #9b0f0f;
            }
            .gray {
                background-color: #ddd;
                color: gray;
            }
            .gray:hover {
                background-color: rgb(172, 172, 172);
                color: white;
            }
            .twitch {
                background-color: #9147ff;
            }
            .twitch:hover {
                background-color: rgb(66, 31, 119);
            }
        }
    }
}
</style>
