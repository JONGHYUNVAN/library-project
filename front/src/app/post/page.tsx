'use client'
import React, {useEffect, useState} from "react";
import axios from 'axios';
import { useRouter } from 'next/navigation';
import { useSearchParams } from "next/navigation";
import {Post} from '../interface/Post'

export default function Post() {
    const router = useRouter();
    const params = useSearchParams();
    const [id, setId] = useState<string | null>(params.get('id'));
    const [post, setPost] = useState<Post | null>(null);


    useEffect(() => {
        async function fetchData() {
                const response = await axios.get(`${process.env.NEXT_PUBLIC_API_URL}/posts/${id}`);
                const dataArray = response.data.data;
                setPost(dataArray);
        }
        fetchData();
    }, [])
    
    return (
    <div>
        <video className="background-video" autoPlay muted loop>
            <source src="/backgroundPost.mp4" type="video/mp4" />
        </video>
        {post? (
                <div className="post">
                    <span className="postTitle">
                        {post.title}
                        <span style={{fontSize: 'clamp(5px, 2vw, 20px)', marginLeft: '20px'}}>
                            {post.authorNickName}
                        </span>
                    </span>

                    <div className="postContent">
                        {post.content}
                    </div>
                    <div className="postBook">
                        <img src={post.bookImage.imageURL}
                             className="postImage"
                             onClick={() => router.push(`/search/?id=${post.bookImage.id}`)}
                        />
                        <span className="postBookTitle">
                            {post.bookImage.title}
                        </span>
                 </div>
                    <div className="postTimes">
                        <p>Created at : {post.createdAt}</p>
                        <p>Updated at : {post.updatedAt}</p>
                    </div>
                </div>
            )
            : (<div className="postNoContent"> No content here </div>)}


    </div>

    )
}