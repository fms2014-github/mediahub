export default function({ $axios }, inject) {
    const accessToken =
        'ya29.a0AfH6SMDIh61B0gU8SvVou5if_r7yFUIfPtClDPWFv4fRgfzm9ZPwf-5Tw1olt_4qF4ESAfN1_nj-7OK1h44wBswrPCnUFWV4njc7Vmmkk2mF_Smsro3Drqpa3VszjTps7bcEQptwhaxLX1lHMIsSjZqcdBXSxzIagPvO'

    const twitchTokenApi = $axios.create({
        headers: { 
            Accept : 'application/vnd.twitchtv.v5+json',
            Authorization: `OAuth ${accessToken}`,
            'Client-ID': client_id,
        },
        
    })

    const client_id = 'oqnfm929440pohis4h4xd1rfr4cd2u'

    const twitchClipsApi = (limit) => {
        return twitchTokenApi.get(`https://api.twitch.tv/kraken/clips/followed?trending=true&limit=${limit}`)
    }

    const twitchScript = {
        twitchClipsApi: (limit) => twitchClipsApi(limit),
    }
    // Inject to context as $api
    inject('twitchApi', twitchScript)
}
