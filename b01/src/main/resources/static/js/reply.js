/*
 * function앞에 async를 붙이는 것이 이 함수의 내부에 비동기로 동작한다는 의미
 * 실제 axis통신하는 부분에는 await를 붙여준다.
 *
 * async함수는 prpmise객체를 우선 리턴한다.
 * 그리고 통신의 결과는 나중에 받는다.
 * */

async function get1(bno) {

    const result = await axios.get(`/replies/list/${bno}`)

    //console.log(result)

    return result;
}


async function getList({bno, page, size, goLast}){

    const result = await axios.get(`/replies/list/${bno}`, {params: {page, size}})

    return result.data
}


async function getList({bno, page, size, goLast}){

    const result = await axios.get(`/replies/list/${bno}`, {params: {page, size}})

    if(goLast){
        const total = result.data.total
        const lastPage = parseInt(Math.ceil(total/size))

        return getList({bno:bno, page:lastPage, size:size})

    }

    return result.data
}


async function addReply(replyObj) {
    const response = await axios.post(`/replies/`,replyObj)
    return response.data
}

async function getReply(rno) {
    const response = await axios.get(`/replies/${rno}`)
    return response.data
}

async function modifyReply(replyObj) {

    const response = await axios.put(`/replies/${replyObj.rno}`, replyObj)
    return response.data
}

async function removeReply(rno) {
    const response = await axios.delete(`/replies/${rno}`)
    return response.data
}
