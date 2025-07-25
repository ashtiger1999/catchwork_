import { axiosApi } from "../api/axiosAPI";
import { useState, useRef } from "react";

const useConfirmEmail = () => {
  const [timeLeft, setTimeLeft] = useState(0);
  const timerRef = useRef(null);

  // 인증번호 발송
  const sendEmail = async (memEmail) => {
    const resp = await axiosApi.post("/member/sendEmail", { memEmail });
    if (resp.status === 200) {
      return true;
    } else {
      return false;
    }
  };

  // 인증번호 확인
  const checkAuthKey = async (memEmail, authKey) => {
    try {
      const resp = await axiosApi.post("/member/checkAuthKey", { memEmail, authKey });
      alert("인증번호가 확인되었습니다.");
      return true;
    } catch (error) {
      if (error.response && error.response.status === 404) {
        alert("유효하지 않은 인증번호입니다.");
      } else {
        alert("인증번호 확인 중 오류가 발생했습니다.");
      }
      return false;
    }
  };

  // 타이머 시작
  const startTimer = () => {
    if (timerRef.current) clearInterval(timerRef.current);
    setTimeLeft(300);
    timerRef.current = setInterval(() => {
      setTimeLeft((prev) => {
        if (prev <= 1) {
          clearInterval(timerRef.current);
          return 0;
        }
        return prev - 1;
      });
    }, 1000);
  };

  // 타이머 멈춤
  const stopTimer = () => {
    if (timerRef.current) clearInterval(timerRef.current);
  };

  return { sendEmail, checkAuthKey, startTimer, stopTimer, timeLeft };
};

export default useConfirmEmail;