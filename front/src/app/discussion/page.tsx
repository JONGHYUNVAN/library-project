'use client'
import React, {useEffect, useState} from "react";
import { useRouter } from 'next/navigation';
import {Post} from '../interface/Post'
import {BookImage} from  '../interface/Post'
import { useSearchParams } from "next/navigation";
import Link from "next/link";
import { Tooltip } from 'react-tooltip'
import Image from 'next/image'


export default function Discussion() {
    const router = useRouter();
    const [posts, setPosts] = useState<Post[] | null>(null);
    const [book, setBook] = useState<BookImage | null>(null);
    const params = useSearchParams();
    const [totalPages,setTotalPages] = useState<number|null>(1);
    const [page, setPage] = useState<number | null>(0);
    const [id, setId] = useState<string | null>(params.get('id'));
    const [tooltip,setTooltip] = useState<boolean|null>(false);

    useEffect(() => {
        const isMobile = window.innerWidth <= 800 || window.innerHeight <= 600;
        const url = id
            ? `${process.env.NEXT_PUBLIC_API_URL}/posts/book-posts/${id}?page=${page}&size=${isMobile ? 6 : 10}`
            : `${process.env.NEXT_PUBLIC_API_URL}/posts?page=${page}&size=${isMobile ? 6 : 10}`;

        fetch(url).then((response) => response.json())
            .then((data) => {
                const dataArray = data.data;
                const totalPagesData = data.totalPages;
                const datas = dataArray.map((item: {
                    id: number,
                    title: string,
                    authorNickName: string,
                    views: number,
                    bookImage: BookImage
                }) => ({
                    id: item.id,
                    title: item.title,
                    authorNickName: item.authorNickName,
                    views: item.views,
                    bookImage: item.bookImage
                }));
                setTotalPages(totalPagesData);
                setPosts(datas);
            })
            .catch((error) => {
                alert(error);
            });

    }, [page]);

    return (
        <div>
            <video className="background-video" autoPlay muted loop>
                <source src="/backgroundPost.mp4" type="video/mp4" />
            </video>
                    <div className="discussion">
                        <div className="postsList">
                            <div className="postListColumn">
                                <p >Post number</p>
                                <h2 >title</h2>
                                <p >author</p>
                                <p >views</p>
                            </div>
                        </div>
                            {posts && posts.map((post, index) => (
                                <div key={index} className="postListPosts">
                                    <p >{post.id}</p>
                                    <Link id={`post-${index}`} href={`/post/?id=${post.id}`} onMouseMove={() => setTooltip(true)}>{post.title}</Link>
                                        {tooltip?(
                                            <>
                                            <Tooltip
                                                place={"top"}
                                                anchorSelect={`#post-${index}`}
                                                //@ts-ignore
                                                content={<Image
                                                    src={post.bookImage.imageURL}
                                                    alt="now loading"
                                                    width={300}
                                                    height={500}
                                                 style={{ width: '12vw', height: '15vw' }}/>}
                                            />
                                            </>
                                            ):(<></>)}
                                    <p>{post.authorNickName}</p>
                                    <p>{post.views}</p>
                                </div>
                            ))}


                        <div className="pageInfos">
                            {(page !== null && page !== undefined && page+1>1) ? (<p onClick={() => setPage(page - 1)}>{"<"}</p>):(<></>)}
                            <p>{page !== null && page !== undefined ? page+1 : 1}/{totalPages}</p>
                            {(page !== null && page !== undefined && totalPages && page+1<totalPages) ? (<p onClick={() => setPage(page + 1)}>{">"}</p>):(<></>)}
                        </div>
                    </div>
        </div>
    )
}