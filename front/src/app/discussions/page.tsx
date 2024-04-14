'use client'
import React, {useEffect, useState} from "react";
import { useRouter } from 'next/navigation';
import {Post} from '../interface/Post'
import {BookImage} from  '../interface/Post'
import { useSearchParams } from "next/navigation";

export default function Write() {
    const router = useRouter();
    const [posts, setPosts] = useState<Post[] | null>(null);
    const [book, setBook] = useState<BookImage | null>(null);
    const params = useSearchParams();
    const [page, setId] = useState<string | null>(params.get('page'));

    useEffect(() => {
        fetch(`${process.env.NEXT_PUBLIC_API_URL}/posts?page=${page}&size=10`)
            .then((response) => response.json())
            .then((data) => {
                const dataArray = data.data;
                const datas = dataArray.map((item: {id:number, title: string, authorNickName: string, bookImage: BookImage }) => ({
                    id: item.id,
                    title: item.title,
                    bookImage: item.bookImage
                }));
                setPosts(datas);
            })
            .catch((error) => {
                console.error(error);
            });
    }, []);
    return (
    <div>
        <video className="background-video" autoPlay muted loop>
            <source src="/backgroundPost.mp4" type="video/mp4" />
        </video>

                <div className="post">

                    <div className="wtiteContent">

                    </div>

                       <div className="postBook">


                        </div>

                    <div className="postTimes">
                    </div>

                </div>
        )


    </div>

    )
}