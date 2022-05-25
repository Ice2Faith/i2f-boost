export function sign(text){
    text=text.length+":"+text;
    let sval0=7;
    let sval1=19;
    let sval2=27;
    let sval3=31;
    let sval4=17;
    let sval5=23;
    let cnt=6;

    let len=text.length;
    for(let i=0;i<len;i++){
        let ch=text.charAt(i);
        let iv=(ch.charCodeAt(0))+Math.trunc(i*100/len);
        if(iv%cnt==0){
            sval0=((sval0+iv)*7)%700;
        }else if(iv%cnt==1){
            sval1=((sval1+iv)*19)%1900;
        }else if(iv%cnt==2){
            sval2=((sval2+iv)*27)%2700;
        }else if(iv%cnt==3){
            sval3=((sval3+iv)*31)%3100;
        }else if(iv%cnt==4){
            sval4=((sval4+iv)*17)%1700;
        }else if(iv%cnt==5){
            sval5=((sval5+iv)*23)%2300;
        }
    }
    return sval0.toString(16)
        +""+sval1.toString(16)
        +""+sval2.toString(16)
        +""+sval3.toString(16)
        +""+sval4.toString(16)
        +""+sval5.toString(16);
}
