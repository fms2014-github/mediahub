export default function({ $axios }, inject) {
    // Create a custom axios instance
    const test = $axios.create({
        baseURL: 'http://localhost:8080/',
        header: {
            // 'Access-Control-Allow-Origin': '*',
            Accept: 'application/json',
            'Content-Type': 'application/json',
        },
    })

    // Inject to context as $api
    inject('test', test)
}
