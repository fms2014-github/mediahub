export default function({ $axios }, inject) {
    // const accessToken =
    //     'ya29.a0AfH6SMC6Erz11saBrKI13dNBEfHYu6-9MwSHm3wpqpCUiufYfVzPf6RNheHyUTHxCtR6E_SNzfoWW89obx5KFgGUmcprfLyAJS_Qa4IxwpCu4GDChSGhU_uzc03gLi_MxXKqbaTo8X7PyMz3CavY4UOloe2VCgXS69Xv'

    const clientId = 'db8sw2xqe82gk1x78mkubkr5xh545p'
    const twitchApi = $axios.create({
        headers: {
            Accept: 'application/vnd.twitchtv.v5+json',
            'Client-ID': clientId,
        },
        baseURL: 'https://api.twitch.tv/kraken/',
    })

    const twitchClipsByChannelApi = (channelName, accessToken) => {
        return twitchApi.get('clips/top', {
            params: {
                channel: channelName,
                period: 'month',
                trending: true,
                limit: 100,
            },
            headers: {
                Authorization: `OAuth ${accessToken}`,
            },
        })
    }
    const twitchVideosApi = (channelId, accessToken) => {
        return twitchApi.get(`channels/${channelId}/videos`, {
            params: {
                limit: 100,
            },
            headers: {
                Authorization: `OAuth ${accessToken}`,
            },
        })
    }

    const twitchStreamsApi = (token) => {
        return twitchApi.get(`streams/followed`, {
            headers: {
                Authorization: `OAuth ${token}`,
            },
        })
    }

    const twitchChannelApi = (channelId, accessToken) => {
        return twitchApi.get(`channels/${channelId}`, {
            headers: {
                Authorization: `OAuth ${accessToken}`,
            },
        })
    }

    const twitchScript = {
        twitchClipsByChannelApi: (channelName, accessToken) => twitchClipsByChannelApi(channelName, accessToken),
        twitchVideosApi: (channelId, accessToken) => twitchVideosApi(channelId, accessToken),
        twitchStreamsApi: (token) => twitchStreamsApi(token),
        twitchChannelApi: (channelId, accessToken) => twitchChannelApi(channelId, accessToken),
    }
    // Inject to context as $api
    inject('twitchApi', twitchScript)
}
