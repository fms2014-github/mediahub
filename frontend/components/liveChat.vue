<template>
    <div id="live-chat">
        <div id="streamer-name">스트리머 A채팅방 입니다.</div>
        <div v-if="messageList !== null" id="message-list">
            <div v-for="item in messageList" :key="item.id" class="receive-message-list">
                <div class="receive-message">
                    <div class="profile">
                        <img class="profile-image" :src="item.profileImage" />
                        <span>{{ item.displayName }}</span>
                    </div>
                    <span>{{ item.textMessage }}</span>
                </div>
                <!-- <div v-if="item.me" class="receive-message my-message">
                    <div class="profile">
                        <img class="profile-image" src="https://via.placeholder.com/24" />
                        <span>{{ item.nickname }}</span>
                    </div>
                    <span>채팅 기록{{ item.message }} 입니다.</span>
                </div>-->
            </div>
        </div>
        <div id="send-message">
            <div id="select-platform">
                <span class="youtube" @click="selectPlatform">YT</span>
                <span class="twitch platform-disable" @click="selectPlatform">TW</span>
            </div>
            <input type="text" name="message" />
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
    },
    data() {
        return {
            messageList: [],
            chatRead: null,
            nextPageToken: ' ',
            pollingIntervalMillis: 5000,
        }
    },
    mounted() {
        const tmi = require('tmi.js')
        // Define configuration options
        const opts = {
            identity: {
                username: 'test_bot',
                password: 'oauth:c33fp5wsu5auevg8in2b02wb26n1qw',
            },
            channels: ['what9honggildong'],
        }
        // Create a client with our options
        const client = new tmi.Client(opts)
        // Register our event handlers (defined below)
        client.on('message', onMessageHandler)
        client.on('connected', onConnectedHandler)
        // Connect to Twitch:
        client.connect()
        // Called every time a message comes in
        function onMessageHandler(target, context, msg, self) {
            if (self) {
                return
            } // Ignore messages from the bot
            // Remove whitespace from chat message
            console.log(context, msg)
            this.messageList.push({
                profileImage: 'https://via.placeholder.com/24',
                textMessage: msg,
                displayName: context['display-name'],
            })
            // const commandName = msg.trim()
            // console.log(commandName)
        }
        // Called every time the bot connects to Twitch chat
        function onConnectedHandler(addr, port) {
            console.log(`* Connected to ${addr}:${port}`)
        }

        // const firstData = (await this.$youtubeApi.youtubeliveChatApi(this)).data
        // for (const i in firstData.items) {
        //     this.messageList.push({
        //         profileImage: firstData.items[i].authorDetails.profileImageUrl,
        //         textMessage: firstData.items[i].snippet.displayMessage,
        //         displayName: firstData.items[i].authorDetails.displayName,
        //     })
        // }
        // this.nextPageToken = firstData.nextPageToken
        // this.pollingIntervalMillis = firstData.pollingIntervalMillis
        this.chatRead = setInterval(() => {
            setTimeout(async () => {
                const data = (await this.$youtubeApi.youtubeliveChatApi(this)).data
                console.log('awefsef', data.items)
                for (const i in data.items) {
                    this.messageList.push({
                        profileImage: data.items[i].authorDetails.profileImageUrl,
                        textMessage: data.items[i].snippet.displayMessage,
                        displayName: data.items[i].authorDetails.displayName,
                    })
                }
                console.log('messageList.push', this.messageList)
                this.nextPageToken = data.nextPageToken
                this.pollingIntervalMillis = data.pollingIntervalMillis
            }, 1000)
        }, this.pollingIntervalMillis)
    },
    destroyed() {
        console.log('aefwefwefdddgggg')
        clearInterval(this.chatRead)
    },
    methods: {
        selectPlatform() {
            const selector = document.querySelectorAll('#select-platform span')
            for (let i = 0; i < selector.length; i++) {
                selector[i].classList.toggle('platform-disable')
            }
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
        color: rgb(150, 150, 150);
        style: solid;
    }
    background-color: white;
    #streamer-name {
        height: 36px;
        padding: 5px;
        background: linear-gradient(
            to bottom,
            rgb(205‬, 205‬, 205‬),
            rgb(185, 185, 185),
            rgb(170, 170, 170),
            rgb(185, 185, 185),
            rgb(205‬, 205‬, 205‬)
        );
    }
    #message-list {
        padding: 0 8px;
        height: calc(100% - 94px);
        overflow: auto;
        @include scrollbar('&');
        border-bottom-width: 1px;
        border-bottom-color: #cdcdcd;
        border-bottom-style: solid;
        img {
            width: 24px;
            height: 24px;
            vertical-align: middle;
            border-radius: 100%;
        }
        span {
            vertical-align: middle;
        }
        .receive-message {
            margin: 12px 0px;
            width: 70%;
            margin-right: calc(30% - 16px);
            padding: 8px;
            background-color: gray;
            border-radius: 8px;
        }
        .my-message {
            margin-left: calc(30% - 16px);
            background-color: rgb(255, 205, 0);
            text-align: right;
        }
    }
    #send-message {
        height: 48px;
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
    }
}
</style>
