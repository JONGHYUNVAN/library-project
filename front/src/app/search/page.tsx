'use client'
import React, {useEffect, useState} from "react";
import axios from 'axios';
import { useSearchParams } from "next/navigation";
import 'react-tooltip/dist/react-tooltip.css'
import { Tooltip } from 'react-tooltip'
import { Suspense } from 'react'
import {Book} from '../interface/Book'
import {PuffLoader, PropagateLoader } from 'react-spinners';
import Image from 'next/image'


export default function Search() {
    const [selected, setStr] = useState("TITLE");
    const [isClicked, setIsClicked] = useState(false);
    const [url, setUrl] = useState("keyword");
    const [input, setInput] = useState("");
    const [selectedBook, setSelectedBook] = useState<Book | null>(null);
    const params = useSearchParams();
    const [id, setId] = useState<string | null>(params.get('id'));
    const [message, setMessage] = useState("Type here");
    const [books, setBooks] = useState<Book[]>([]);
    const [loading, setLoading] = useState(false);
    const [searching, setSearching] = useState(false);
    const previousClickedElement = React.useRef<HTMLElement | null>(null);


    useEffect(() => {
        async function fetchData() {
            setLoading(true);
            setSearching(true);
            if(id) {
                const token = localStorage.getItem('accessToken');
                let config = {};

                if (token) {
                    config = {
                        headers: {
                            'Authorization': `Bearer ${token}`
                        },
                     withCredentials: true 
                    }
                }
                const response = await axios.get(`${process.env.NEXT_PUBLIC_API_URL}/books/${id}`,config);
                setSelectedBook(response.data.data);
                setLoading(false);
                setMessage(response.data.data.title);
            }
        }
        fetchData();
    }, [id])
    const handleTitleClick = () => {
        setStr("TITLE");
        setUrl("keyword")
    };

    const handleAuthorClick = () => {
        setStr("AUTHOR");
        setUrl("author")
    };

    const handlePublisherClick = () => {
        setStr("PUBLISHER");
        setUrl("publisher")
    };
    const handleMouseDown = () => {
        setIsClicked(true);
    };

    const handleMouseUp = () => {
        setIsClicked(false);
    };
    const handleFormSubmit = (event: React.FormEvent) => {
        event.preventDefault();
        handleButtonClick();
    };
    const handleButtonClick = async () => {
        try {
            const response = await axios.get(`${process.env.NEXT_PUBLIC_API_URL}/books/${url}/${input}`);
            const dataArray = response.data.data;
            const books = dataArray.map((item: { id: string; title: string; imageURL: string}) => ({
                id: item.id,
                title: item.title,
                imageURL:item.imageURL
            }));
            setBooks(books);
        }
        catch(error){
            console.error('Error in axios.get', error);
        }
    };
    const handleInputChange = (event:React.ChangeEvent<HTMLInputElement>) => {
        setInput(event.target.value);
    };

    const handleTextClick = (event: React.MouseEvent<HTMLDivElement>, book: Book) => {
        if(book.id !== id) {
            setSelectedBook(book);
            setId(book.id);
            if (previousClickedElement.current) {
                previousClickedElement.current.classList.remove('textClicked');
            }
            const currentElement = event.currentTarget as HTMLElement;
            currentElement.classList.add('textClicked');

            previousClickedElement.current = currentElement;
        }
    };
    
    return (
        <div>

            <div className="smallTitle" style={{marginTop:'0.1vh',marginBottom: '0.1vh'}}>
                <h4>search</h4>
            </div>
            
            <div className="search-container" style={{ display: 'flex', flexDirection: 'row', alignItems: 'center' }}>
                <h2 style={{ fontFamily: 'Long Cang, cursive', fontSize: 'clamp(20px,2vw,40px)',color: 'papayawhip',marginRight: '1vw',marginLeft: '3vw',minWidth:'10vw' }} className="search-label"> Search by</h2>
                <div className="buttons" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', gap: '0.1vh', marginRight: '1vw' }}>
                    <button className="title-button" onClick={handleTitleClick}>
                        {'\u00A0\u00A0\u00A0\u00A0Title\u00A0\u00A0\u00A0\u00A0'}
                    </button>
                    <button className="author-button" onClick={handleAuthorClick}>
                        {'\u00A0\u00A0Author\u00A0\u00A0'}
                    </button>
                    <button className="publisher-button" onClick={handlePublisherClick}>
                        Publisher
                    </button>
                </div>
                <div className="selected-wrapper" style={{ width: '10%', justifyContent: 'center', alignItems: 'center', height: '5vh', marginRight: '1vw' }}>
                    <div className="selected" style={{ fontFamily: 'Nanum Pen Script',textAlign: 'center',fontSize: 'clamp(20px,2vw,100px)',color:'gold' }}>
                        <div>
                            {selected}
                        </div>
                    </div>
                </div>
                <div className="search" style={{ width: '45%',marginRight: '4vw' }}>
                    <form onSubmit={handleFormSubmit}>
                        <input type="text"
                               placeholder={message}
                               style={{ width: '100%', height: '2.5vw', fontSize: '2vw' }}
                               onChange={handleInputChange} />
                    </form>
                </div>

                <Image
                    src="/search.gif"
                    alt="image"
                    width={500}
                    height={300}
                    style={{ width: 'clamp(10vw,10vw,5px)', height: 'clamp(10vw,10vw,5px)'}}
                    onClick={handleButtonClick}
                    className={isClicked ? 'searchImage clicked' : 'searchImage'}
                    onMouseDown={handleMouseDown}
                    onMouseUp={handleMouseUp}
                    onMouseLeave={handleMouseUp}/>
            </div>


            <div className="searchResult">
                {books.map((book, index) => (
                    <div  key={index} className="searchResultsText"  onClick={((event) => handleTextClick(event,book))}>
                        <a id={`book-${index}` } className ="searchResultText">{book.title}</a>
                        <Tooltip
                            place={"left"}
                            anchorSelect={`#book-${index}`}
                            //@ts-ignore
                            content={<img src={book.imageURL} alt={book.title} style={{ width: '8vw', height: '10vw' }}/>}
                        />
                    </div>

                ))}
            </div>

            <div className="bookDetail">
                {searching ? (
                        loading ? (
                            <div className="bookDetailContent" style={{ display: 'flex', flexDirection: 'row'}}>
                                <div className="book-image-big" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center',marginTop: '50px'}} >
                                    <PuffLoader size={'40vw'} color="#DAA520" />
                                </div>
                                <div className="searchInfo" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center'}}>
                                     <PropagateLoader size={'2vw'} color="#FFD700"/>
                                </div>
                            </div>)
                                         :
                                (
                        selectedBook?(
                        <div className="bookDetailContent">
                            <img src={selectedBook.imageURL} alt={selectedBook.title} className="book-image-big"/>
                            <div className="searchInfo">
                                <h2 >{selectedBook.title }</h2>
                                <p>장르: {selectedBook.genreName}</p>
                                <p>저자: {selectedBook.author}</p>
                                <p>출판사: {selectedBook.publisher}</p>
                                <p>상태: {selectedBook.status}</p>
                                <p>소장 도서관: {selectedBook.libraryName}</p>
                            </div>
                        </div>):(<div></div>)
                                 )
                ) : (
                    <div></div>
                )}
            </div>
            <video  poster={"/posterSearch.png"} className="background-video" autoPlay muted loop>
                <source src="/backgroundSearch.mp4" type="video/mp4" />
            </video>

        </div>
        
    );
}