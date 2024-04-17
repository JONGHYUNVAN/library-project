'use client'
import React, {useEffect, useState} from "react";
import axios from 'axios';
import { useRouter } from 'next/navigation';
import { useSearchParams } from "next/navigation";
import {Post} from '../interface/Post'
import {Comment} from '../interface/Post'
import Link from "next/link";
import {useAppSelector } from "@/redux/hooks";


export default function Post() {
    const router = useRouter();
    const params = useSearchParams();
    const [id, ] = useState<string | null>(params.get('id'));
    const [post, setPost] = useState<Post | null>(null);
    const [comments, setComments] = useState<Comment[]>([]);
    const [totalCommentPages, setTotalCommentPages] = useState<number | null>(0);
    const [commentPage, setCommentPage] = useState<number | null>(0);
    const loggedIn = useAppSelector((state) => state.authReducer.value);
    const [newComment, setNewComment] = React.useState('');
    const [submitting, setSubmitting] = useState(false);

    useEffect(() => {
        async function fetchData() {
            const response = await axios.get(`${process.env.NEXT_PUBLIC_API_URL}/posts/${id}`, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                }
            });
            const dataArray = response.data.data;
                setPost(dataArray);
        }
        fetchData();
    }, [])
    useEffect(() => {
        const isMobile = window.innerWidth <= 800 || window.innerHeight <= 600;
        fetch(`${process.env.NEXT_PUBLIC_API_URL}/comments/post-comments/${id}?commentcommentPage=${commentPage}&size=${isMobile ? 5 : 10}`)
                .then((response) => response.json())
                .then((data) => {
                    const dataArray = data.data;
                    const totalcommentPagesData = data.totalPages;
                    const datas = dataArray.map((item: {
                        id: number,
                        content: string,
                        authorNickName: string,
                        createdAt: string,
                        updatedAt: string
                    }) => ({
                        id: item.id,
                        content: item.content,
                        authorNickName: item.authorNickName,
                        createdAt: item.createdAt,
                        updatedAt: item.updatedAt
                    }));
                    setTotalCommentPages(totalcommentPagesData);
                    setComments(datas);
                })
                .catch((error) => {
                alert(error);
                });
    }, []);
    const handleDeletePost = async () => {
        if (window.confirm("Your written content will be deleted. Do you wish to continue?")) {
            try {
                const response = await axios.delete(`${process.env.NEXT_PUBLIC_API_URL}/posts/${id}`, {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                    }
                });
                if (response.status === 204) {
                    window.history.back();
                }
            }
            catch (error) {
                alert(error);
            }
        }
    };
    const handleDeleteComment = async (commentId:number) => {
        if (window.confirm("Your written content will be deleted. Do you wish to continue?")) {
            try {
                const response = await axios.delete(`${process.env.NEXT_PUBLIC_API_URL}/comments/${commentId}`, {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                    }
                });
                if (response.status === 204) {
                    window.location.reload();
                }
            }
            catch (error) {
                alert(error);
            }
        }
    };
    const handleNewCommentInput = (event: React.ChangeEvent<HTMLInputElement>) => {
        setNewComment(event.target.value);
    }
    const handleNewCommentSubmit = async () => {
        setSubmitting(true);
        const commentData = {
            postId: id,
            content: newComment
        }
        try {
            const response = await axios.post(`${process.env.NEXT_PUBLIC_API_URL}/comments`, commentData,{
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                }
            });
            if (response.status === 201) {
                window.location.reload();
            }
        }
        catch (error) {
            alert(error);
            setSubmitting(false);
        }
      setSubmitting(false);
    }


        return (
    <div>
        <video className="background-video" autoPlay muted loop>
            <source src="/backgroundPost.mp4" type="video/mp4" />
        </video>
        {post? (
                <div className="post">
                    <span className="postTitle">
                        {post.title}
                    </span>

                    <pre className="postContent">
                        {post.content}
                    </pre>
                    <div className="postProfile">
                        <img src={`/profile${post.profile}.jpg`}
                             className="postProfileImage"
                        />
                        <span className="postAuthor">
                            {post.authorNickName}
                        </span>
                    </div>
                    
                    {post.isAuthor ?
                        (<>
                            <div className="postModify">
                                <Link href={`/modify?id=${id}`}>modify</Link>
                            </div>
                            <div className="postDelete">
                                <p onClick={() => {
                                    handleDeletePost();
                                }}>delete</p>
                            </div>
                        </>
                        ):(<></>)
                    }

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
                        <p>Posted : {post.createdAt}</p>
                        <p>Last update : {post.updatedAt}</p>
                    </div>
                </div>
            )
            
            : (<div className="postNoContent"> No content here </div>)}
        {loggedIn === "In" ? (
        <div className="newCommentBox">
            <input
                type="text"
                name="name"
                placeholder="type your comment here"
                onChange={handleNewCommentInput}
                className="newComment"
            />
            <p onClick={!submitting && newComment != "" ? () => {} : handleNewCommentSubmit}>
                {!submitting && newComment != "" ? 'submit' : ''}
            </p>
        </div>):(<></>)}

        {comments ? (
            <>
                <div style={{marginBottom: '2vh'}}>
                    {comments.map((comment: {
                        id: number,
                        content: string,
                        authorNickName: string,
                        createdAt: string,
                        updatedAt: string
                    }) => (
                        <div key={comment.id} className="commentBox">
                            <div className="comment">
                                <p>{comment.content}</p>
                            <h2>{comment.authorNickName}</h2>
                            <h3 onClick={() => {
                                handleDeleteComment(comment.id);
                            }}>delete</h3>
                        </div>
                        { window.innerWidth <= 800 || window.innerHeight <= 600 ? (<></>) : (
                            <div className="commentAbout">
                                <small>Commented : {comment.createdAt}</small>
                                <br/>
                                <small>Last update : {comment.updatedAt}</small>
                            </div>
                        )}
                    </div>
                ))}

                <div className="commentPageInfos">
                    <p
                        onClick={() => setCommentPage( commentPage==null ? 1 : commentPage - 1)}
                        className={commentPage !== null && commentPage !== undefined && commentPage + 1 > 1 ? '' : 'invisible'}
                    >
                        {"<"}
                    </p>
                    <h3>{commentPage !== null && commentPage !== undefined ? commentPage + 1 : 1} / {totalCommentPages}</h3>
                    <p
                        onClick={() => setCommentPage(commentPage==null ? 1 : commentPage + 1)}
                        className={commentPage !== null && commentPage !== undefined && totalCommentPages && commentPage + 1 < totalCommentPages ? '' : 'invisible'}
                    >
                        {">"}
                    </p>
                </div>
            </div>
                <div style={{minHeight: '8vh'}}></div>
            </>
        ) : (<></>)}


    </div>

    )
}