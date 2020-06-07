<template>
    <div id="live-chat">
        <div v-if="youtube !== ''" id="total-chat">
            <div id="streamer-name">스트리머 A채팅방 입니다.</div>
            <div v-if="messageList !== null" id="message-list">
                <div v-for="item in messageList" :key="item.id" class="receive-message-list">
                    <div class="receive-message">
                        <div class="profile">
                            <img class="profile-image" :src="item.provider === 'google' ? '/YouTube.png' : '/Twitch.png'" />
                            <span>{{ item.displayName }}</span>
                        </div>
                        <span style="display: inline-block; margin-top: 8px;">{{ item.textMessage }}</span>
                    </div>
                </div>
            </div>
            <div id="send-message">
                <div id="select-platform" @click="selectPlatform">
                    <span v-if="youtube !== ''" class="youtube">YT</span>
                    <span v-if="twitch !== ''" class="twitch platform-disable">TW</span>
                </div>
                <input v-if="sendSelect === 'TW'" v-model="message" type="text" name="message" @keyup.enter="sendMessage(sendSelect)" />
                <div v-if="twitch !== '' && sendSelect === 'YT'" id="twitch-chat-send">
                    <iframe
                        :id="twitch"
                        frameborder="0"
                        scrolling="yes"
                        :src="'https://www.twitch.tv/embed/' + twitch + '/chat?parent=localhost'"
                        width="260"
                        height="94"
                    >
                    </iframe>
                    <div id="disable-twitch-emotion" title="트위치 이모션 기능은 사용할 수 없습니다"></div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        liveChatId: {
            type: String,
            required: true,
        },
        youtube: {
            type: String,
            required: true,
        },
        twitch: {
            type: String,
            required: true,
        },
    },
    data() {
        return {
            tmi: null,
            client: null,
            messageList: [],
            chatRead: null,
            nextPageToken: ' ',
            pollingIntervalMillis: 5000,
            sendSelect: 'TW',
            message: '',
        }
    },
    async mounted() {
        // console.log('chat twitch', this.twitch)
        const objDiv = document.getElementById('message-list')
        if (this.twitch !== '') {
            this.tmi = require('tmi.js')

            // Define configuration options
            const opts = {
                identity: {
                    username: 'test_bot',
                    password: 'oauth:c33fp5wsu5auevg8in2b02wb26n1qw',
                },
                channels: [this.twitch],
            }
            // Create a client with our options
            this.client = new tmi.Client(opts)
            // Register our event handlers (defined below)
            // client.on('message', onMessageHandler)
            this.client.on('message', (target, context, msg, self) => {
                if (self) {
                    return
                } // Ignore messages from the bot
                // Remove whitespace from chat message
                // // console.log('this scope', this.messageList)
                // // console.log(context, msg)
                this.messageList.push({
                    profileImage: 'https://via.placeholder.com/24',
                    textMessage: msg,
                    displayName: context['display-name'],
                    provider: 'twitch',
                })
                setTimeout(() => {
                    objDiv.scrollTop = objDiv.scrollHeight + 1000
                }, 100)
                // const commandName = msg.trim()
                // // console.log(commandName)
            })
            this.client.on('connected', onConnectedHandler)
            // Connect to Twitch:
            this.client.connect()
            // Called every time a message comes in
            // Called every time the bot connects to Twitch chat
            // eslint-disable-next-line no-inner-declarations
            function onConnectedHandler(addr, port) {
                // console.log(`* Connected to ${addr}:${port}`)
            }
        }
        if (this.youtube !== '') {
            const firstData = (await this.$youtubeApi.youtubeliveChatApi(this)).data
            for (const i in firstData.items) {
                this.messageList.push({
                    profileImage: firstData.items[i].authorDetails.profileImageUrl,
                    textMessage: firstData.items[i].snippet.displayMessage,
                    displayName: firstData.items[i].authorDetails.displayName,
                    provider: 'google',
                })
            }
            this.nextPageToken = firstData.nextPageToken
            this.pollingIntervalMillis = firstData.pollingIntervalMillis

            this.chatRead = setInterval(async () => {
                objDiv.scrollTop = objDiv.scrollHeight
                const { data } = await this.$youtubeApi.youtubeliveChatApi({
                    liveChatId: this.liveChatId,
                    pageToken: this.nextPageToken,
                    pollingIntervalMillis: this.pollingIntervalMillis,
                })
                // console.log(data)
                if (data.pageInfo.totalResults > 0) {
                    for (const i in data.items) {
                        this.messageList.push({
                            profileImage: data.items[i].authorDetails.profileImageUrl,
                            textMessage: data.items[i].snippet.displayMessage,
                            displayName: data.items[i].authorDetails.displayName,
                            provider: 'google',
                        })
                    }
                }
                this.nextPageToken = data.nextPageToken
                this.pollingIntervalMillis = data.pollingIntervalMillis - 2000
                setTimeout(() => {
                    objDiv.scrollTop = objDiv.scrollHeight + 1000
                }, 100)
            }, this.pollingIntervalMillis)
        }
    },
    destroyed() {
        if (this.youtube !== '') {
            clearInterval(this.chatRead)
        }
        if (this.client !== null) {
            this.client.removeAllListeners('message')
            this.client.removeAllListeners('connected')
        }
    },
    methods: {
        selectPlatform() {
            const selector = document.querySelectorAll('#select-platform span')
            if (selector.length === 2) {
                for (let i = 0; i < selector.length; i++) {
                    selector[i].classList.toggle('platform-disable')
                }
                this.sendSelect = document.querySelector('.platform-disable').textContent
                // console.log(this.sendSelect)
            }
        },
        sendMessage(platform) {
            // console.log(this.message)
            this.$youtubeApi.youtubeliveChatInsertApi({ liveChatId: this.liveChatId, msg: this.message }).then((res) => {
                // console.log(res)
            })
            this.message = ''
        },
    },
}
</script>

<style lang="scss" scoped>
@import '~/assets/commonMixin';
#live-chat {
    display: inline-block;
    min-width: 300px;
    border: {
        width: 1px;
        color: rgba(150, 150, 150, 0.45);
        style: solid;
        radius: 8px;
    }
    background-color: white;
    box-shadow: 0px 0px 3px 1px rgba(140, 140, 140, 0.7);
    padding: 4px;
    #total-chat {
        height: 100%;
        #streamer-name {
            line-height: 36px;
            text-align: center;
            height: 36px;
            padding: 5px;
            vertical-align: middle;
            border: {
                width: 0 0 1px 0;
                color: rgb(150, 150, 150);
                style: solid;
            }
        }
        #message-list {
            position: relative;
            padding: 0 8px;
            height: calc(100% - 94px);
            overflow: auto;
            @include scrollbar('&');
            z-index: 100;
            border-bottom-width: 1px;
            border-bottom-color: #cdcdcd;
            border-bottom-style: solid;
            background-color: white;
            img {
                width: 24px;
                height: 24px;
                vertical-align: middle;
                border-radius: 100%;
            }
            span {
                vertical-align: middle;
            }
            border: {
                width: 1px;
                color: rgba(150, 150, 150, 0.45);
                style: solid;
            }
            .receive-message {
                margin: 12px 0px;
                width: 80%;
                margin-right: calc(20% - 16px);
                padding: 8px;
                background-color: rgb(230, 230, 230);
                border-radius: 8px;
                box-shadow: 0px 0px 4px 1px rgba(150, 150, 150, 1);
            }
        }
        #send-message {
            height: 52px;
            margin-top: 4px;

            #select-platform {
                display: inline-block;
                width: 10%;
                margin: 2px;
                span {
                    display: inline-block;
                    width: 100%;
                    padding: 2px 1px;
                    margin: 1px;
                    vertical-align: top;
                    text-align: center;
                    border-radius: 3px;
                    font: {
                        size: 0.8rem;
                    }
                }
                .youtube {
                    background-color: $youtube-color;
                    color: white;
                }
                .twitch {
                    background-color: $twitch-color;
                    color: white;
                }
                .platform-disable {
                    background-color: rgb(150, 150, 150);
                }
            }
            input[name='message'] {
                vertical-align: top;
                width: calc(100% - 10% - 25px);
                margin: 2px 0;
                padding: 10px 5px;
            }
            #twitch-chat-send {
                position: absolute;
                bottom: 0px;
                right: 0px;
                z-index: 1;
                #disable-twitch-emotion {
                    position: absolute;
                    right: 16px;
                    bottom: 8px;
                    width: 30px;
                    height: 30px;
                    background-color: #f2f2f2;
                }
            }
        }
    }
}
</style>
