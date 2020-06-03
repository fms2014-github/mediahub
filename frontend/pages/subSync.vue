<template>
    <div id="subscription-sync">
        <div id="content-wrap">
            <h2>환영합니다.</h2>
            <h2>구독 정보를 동기화하시려면 아래 버튼을 눌러주세요.</h2>
            <button id="youtube-sync" @click="syncYoutube">
                <img src="../assets/icon/YouTube.png" />
                <span>YouTube</span>
            </button>
            <button id="twitch-sync" @click="syncTwitch">
                <img src="../assets/icon/Twitch.png" />
                <span>Twitch</span>
            </button>
        </div>
    </div>
</template>

<script>
export default {
    layout: 'cover',
    middleware: 'firstLogin',
    methods: {
        syncYoutube() {
            window.location.href =
                'https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/youtube&state=statparameter_passthrough_value&access_type=offline&response_type=code&client_id=412300629283-sd7m25vk2m7uijmt9lfpjkscvq3sr6vv.apps.googleusercontent.com&redirect_uri=http://localhost:3000/youtube/code&prompt=consent'
        },
        syncTwitch() {
            window.location.href =
                'https://id.twitch.tv/oauth2/authorize?client_id=db8sw2xqe82gk1x78mkubkr5xh545p&response_type=code&scope=channel_check_subscription channel_commercial channel_editor channel_feed_edit channel_feed_read channel_read channel_stream channel_subscriptions collections_edit communities_edit communities_moderate openid user_blocks_edit user_blocks_read user_follows_edit user_read user_subscriptions viewing_activity_read&redirect_uri=http://localhost:3000/twitch/code'
        },
    },
}
</script>

<style lang="scss" scoped>
@import '@/assets/commonMixin.scss';
#subscription-sync {
    #content-wrap {
        display: flex;
        flex-direction: column;
        justify-content: center;
        height: 100vh;
        width: 100vw;
        transform: translateY(-10%);
        h2 {
            text-align: center;
        }
        button {
            padding: 0px;
            margin: 0px;
        }
        #youtube-sync {
            width: 250px;
            height: 62px;
            margin: 10px 0 10px 50%;
            transform: translateX(-50%);
            background-color: $youtube-color;
            img {
                background-color: rgb(255, 255, 255);
                padding: 8px;
                margin: 0 8px;
                vertical-align: middle;
            }
            span {
                vertical-align: middle;
                font: {
                    size: 0.9rem;
                    weight: 700;
                }
                color: rgb(230, 230, 230);
            }
            border: {
                width: 0px;
                radius: 10px;
            }
        }
        #twitch-sync {
            background-color: $twitch-color;
            margin: 10px 0 0 50%;
            transform: translateX(-50%);
            width: 250px;
            height: 62px;
            img {
                margin: 0 8px;
                vertical-align: middle;
            }
            span {
                vertical-align: middle;
                font: {
                    size: 0.9rem;
                    weight: 700;
                }
                color: rgb(30, 30, 30);
            }
            border: {
                width: 0px;
                radius: 10px;
            }
        }
        #youtube-sync:hover {
            background-color: rgb(190, 0, 0);
        }
        #twitch-sync:hover {
            background-color: rgb(122, 62, 212);
        }
    }
}
</style>
