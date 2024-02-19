import {NextResponse} from "next/server";

export async function POST(request: Request){
    const googleTokenUrl = "https://oauth2.googleapis.com/token";
    const url = new URL(request.url)
    const code = url.searchParams.get('code')

    const res = await fetch(googleTokenUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'
        },
        body: new URLSearchParams({
            grant_type: 'authorization_code',
            client_id: process.env.NEXT_PUBLIC_GOOGLE_CLIENT_ID!,
            client_secret:process.env.LIB_GOOGLE_CLIENT_SECRET!,
            redirect_uri: process.env.NEXT_PUBLIC_GOOGLE_REDIRECT_URL!,
            code: code!,
        }).toString()
    })
    const data = await res.json();

    return NextResponse.json(data);
}