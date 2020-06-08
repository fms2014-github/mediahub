export default function({ $axios }, inject) {
    // Create a custom axios instance
    const loginAxios = $axios.create({
        baseURL: 'http://localhost:3000/',
        header: {
            // 'Access-Control-Allow-Origin': '*',
            Accept: 'application/json',
            'Content-Type': 'application/json',
        },
    })

    // Inject to context as $api
    inject('loginAxios', loginAxios)
}
