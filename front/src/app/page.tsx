import Link from 'next/link';


export default function Home() {
  let name = 'J.H.VAN';
  return (
      <>
          <head>
              <title> Welcome! </title>
          </head>

          <div className="main-page">
              <div className="title" >
                  <h2 className="titleLetter">Library project</h2>
                  <span className="title-sub">by {name}</span>
              </div>
              <Link href="/ranking">
              <video poster={"/backposter.png"} className="background-video" autoPlay muted loop >
                  <source src="/background.mp4" type="video/mp4" />
                </video>
              </Link>
          </div>

      </>
  );
}
