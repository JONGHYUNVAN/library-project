import {NextResponse} from "next/server";

export async function POST(request: Request){
    const kakaoTokenUrl = "https://kauth.kakao.com/oauth/token";
    const url = new URL(request.url)
    const code = url.searchParams.get('code')

    const res = await fetch(kakaoTokenUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'
        },
        body: new URLSearchParams({
            grant_type: 'authorization_code',
            client_id: process.env.NEXT_PUBLIC_KAKAO_CLIENT_ID!,
            redirect_uri: process.env.NEXT_PUBLIC_KAKAO_REDIRECT_URL!,
            code: code!,
        }).toString()
    })
    const data = await res.json();

    return NextResponse.json(data);
}