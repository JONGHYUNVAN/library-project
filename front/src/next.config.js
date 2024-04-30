/** @type {import('next').NextConfig} */
const nextConfig = {}

module.exports = {
    experimental: {
        missingSuspenseWithCSRBailout: false,
    },
    images: {
        remotePatterns: [
            {
                protocol: 'https',
                hostname: '**', // 모든 도메인 허용
                port: '',
                pathname: '**', // 모든 경로 허용
            },
        ],
    },
}