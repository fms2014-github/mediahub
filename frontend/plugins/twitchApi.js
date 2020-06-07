export default function({ $axios }, inject) {
    const accessToken =
        'ya29.a0AfH6SMAdIq5QvIa_VNT8KxO3WhHa7LwjjDinDPmUEgvpbKZpxDNHQkvWJSiF2ivMTzq4ilTUUsy6UGFTTRswHjc4PXr5NWkavTxKfWuPZLvNL7IMb0KCMrOrQ6Js-rKzV_TDGeNoOipMximUfL9omys4sh1aQvsdFbPC'

    const clientId = 'db8sw2xqe82gk1x78mkubkr5xh545p'
    const twitchApi = $axios.create({
        headers: {
            Accept: 'application/vnd.twitchtv.v5+json',
            Authorization: `OAuth ${accessToken}`,
            'Client-ID': clientId,
        },
        baseURL: 'https://api.twitch.tv/kraken/',
    })

    const twitchClipsByChannelApi = (channelName) => {
        return twitchApi.get('clips/top', {
            params: {
                channel: channelName,
                period: month,
                trending: true,
                limit: 100,
            },
        })
    }
    const twitchVideosApi = (channelId) => {
        return twitchApi.get(`channels/${channelId}/videos`)
    }

    const twitchStreamsApi = (token) => {
        return twitchApi.get(`streams/followed`, {
            headers: {
                Authorization: `OAuth ${token}`,
            },
        })
    }

    const twitchChannelApi = (channelId) => {
        return twitchApi.get(`channels/${channelId}`)
    }

    const twitchScript = {
        twitchClipsByChannelApi: (channelName) => twitchClipsByChannelApi(channelName),
        twitchVideosApi: (channelId) => twitchVideosApi(channelId),
        twitchStreamsApi: (token) => twitchStreamsApi(token),
        twitchChannelApi: (channelId) => twitchChannelApi(channelId),
    }
    // Inject to context as $api
    inject('twitchApi', twitchScript)
}
