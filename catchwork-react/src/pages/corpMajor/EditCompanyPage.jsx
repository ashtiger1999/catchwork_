import { useNavigate } from "react-router-dom";
import { useEffect, useState, useRef } from "react";
import SectionHeader from "../../components/common/SectionHeader";
import FloatButton from "../../components/common/FloatButton";
import { FLOAT_BUTTON_PRESETS } from "../../components/common/ButtonConfigs";
import useLoginMember from "../../stores/loginMember";
import { axiosApi } from "../../api/axiosAPI";
import "./EditCompanyPage.css";
import { extractPixelColor } from "../../api/extractPixelColor";

const EditCompanyPage = () => {
  const corpLogoUrl = import.meta.env.VITE_FILE_COMPANY_IMG_URL;
  const navigate = useNavigate();
  const { loginMember, setLoginMember } = useLoginMember();

  const [company, setCompany] = useState(null);
  const [loading, setLoading] = useState(true);
  const [corpNo, setCorpNo] = useState("");
  const [corpName, setCorpName] = useState("");
  const [corpType, setCorpType] = useState("");
  const [corpRegNo, setCorpRegNo] = useState("");
  const [corpCeoName, setCorpCeoName] = useState("");
  const [corpAddr, setCorpAddr] = useState("");
  const [corpOpenDate, setCorpOpenDate] = useState("");
  const [corpHomeLink, setCorpHomeLink] = useState("");
  const [corpBm, setCorpBm] = useState("");
  const [corpDetail, setCorpDetail] = useState("");
  const [corpBenefit, setCorpBenefit] = useState("");
  const [corpBenefitDetail, setCorpBenefitDetail] = useState("");
  const [corpLogoFile, setCorpLogoFile] = useState(null); // 업로드할 새 파일
  const [corpLogoPreview, setCorpLogoPreview] = useState(company?.corpLogo); // 미리보기 URL
  const fileInputRef = useRef(null);
  const [pixelColor, setPixelColor] = useState("transparent");

  useEffect(() => {
    setLoginMember(); // 로그인 정보 설정
  }, []);

  // company 로딩 후 corpLogoPreview 초기화
  useEffect(() => {
    if (company?.corpLogo && !corpLogoPreview) {
      setCorpLogoPreview(`${corpLogoUrl}/${company.corpLogo}`);
    }
  }, [company, corpLogoUrl]);

  // 기업 로고 색상 추출
  useEffect(() => {
    if (corpLogoPreview || company?.corpLogo) {
      const imageUrl = corpLogoPreview
        ? corpLogoPreview
        : `${corpLogoUrl}/${company.corpLogo}`;
      extractPixelColor(imageUrl, 1, 1).then((color) => {
        setPixelColor(color.hex);
      });
    }
  }, [corpLogoUrl, corpLogoPreview]);

  //기업 정보
  useEffect(() => {
    const CompanyDetail = async () => {
      if (!loginMember?.memNo) return;

      try {
        const res = await axiosApi.get("/corpcompany/detail", {
          params: { memNo: loginMember.memNo },
        });
        //console.log("기업 정보:", res.data);
        setCompany(res.data);
      } catch (err) {
        console.error("기업 정보 불러오기 실패", err);
      } finally {
        setLoading(false);
      }
    };
    if (loginMember?.memNo) {
      CompanyDetail(); // 이동 시 새로 불러오기
    }
    CompanyDetail();
  }, [loginMember]);

  //불러온 기업 정보로 state 초기화
  useEffect(() => {
    if (company) {
      setCorpNo(company.corpNo);
      setCorpName(company.corpName || "");
      setCorpType(company.corpType || "");
      setCorpRegNo(company.corpRegNo || "");
      setCorpCeoName(company.corpCeoName || "");
      setCorpAddr(company.corpAddr || "");
      setCorpOpenDate(company.corpOpenDate || "");
      setCorpHomeLink(company.corpHomeLink || "");
      setCorpBm(company.corpBm || "");
      setCorpDetail(company.corpDetail || "");
      setCorpBenefit(company.corpBenefit || "");
      setCorpBenefitDetail(company.corpBenefitDetail || "");
    }
  }, [company]);

  //로딩 상태
  if (loading) {
    return (
      <main className="container">
        <SectionHeader title="기업 정보 수정" />
        <p>불러오는 중입니다...</p>
      </main>
    );
  }

  //기업 정보 없음
  if (!company) {
    return (
      <main className="container">
        <SectionHeader title="기업 정보 수정" />
        <p>존재하지 않는 기업입니다.</p>
      </main>
    );
  }

  //취소버튼 누르면 디테일 페이지로 이동
  const handleCancel = () => {
    navigate(`/corpcompany/detail`);
  };
  const corpInfo = {
    corpNo,
    corpName,
    corpType,
    corpRegNo,
    corpCeoName,
    corpAddr,
    corpOpenDate,
    corpHomeLink,
    corpBm,
    corpDetail,
    corpBenefit,
    corpBenefitDetail,
  };

  const formData = new FormData();
  formData.append(
    "corpInfo",
    new Blob([JSON.stringify(corpInfo)], { type: "application/json" })
  );

  if (corpLogoFile) {
    formData.append("corpLogoFile", corpLogoFile);
  }

  // 기업 정보 수정
  const handleEdit = async () => {
    const corpInfo = {
      corpNo,
      corpName,
      corpType,
      corpRegNo,
      corpCeoName,
      corpAddr,
      corpOpenDate,
      corpHomeLink,
      corpBm,
      corpDetail,
      corpBenefit,
      corpBenefitDetail,
    };

    const formData = new FormData();
    formData.append(
      "corpInfo",
      new Blob([JSON.stringify(corpInfo)], { type: "application/json" })
    );

    if (corpLogoFile) {
      formData.append("corpLogoFile", corpLogoFile);
    }

    try {
      await axiosApi.post("/corpcompany/update", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });

      alert("수정이 완료되었습니다.");
      navigate("/corpcompany/detail");
    } catch (error) {
      console.error("수정 실패:", error);
      alert("수정 중 오류가 발생했습니다.");
    }
  };

  // 카카오맵 주소 핸들러
  const handleAddressSearch = () => {
    new window.daum.Postcode({
      oncomplete: function (data) {
        const fullAddress = data.address;
        setCorpAddr(fullAddress);
      },
    }).open();
  };

  const handleLogoClick = () => {
    fileInputRef.current?.click(); // 이미지 클릭 시 파일 업로드 창 열기
  };

  const handleLogoChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setCorpLogoFile(file);
      const previewUrl = URL.createObjectURL(file);
      setCorpLogoPreview(previewUrl);
    }
  };
  return (
    <main className="corp-container">
      <SectionHeader title="기업 정보 수정" />

      <div className="company-detail-header">
        <div className="company-header-left" onClick={handleLogoClick}>
          <div
            className="company-header-image-background"
            style={{
              backgroundColor: pixelColor, // 추출된 색상 사용
            }}
          />
          <img
            src={
              corpLogoPreview
                ? corpLogoPreview
                : `${corpLogoUrl}/${company.corpLogo}`
            }
            alt="기업로고"
            className="company-detail-logo"
          />
          {/* <div className="image-overlay">
              <i className="fas fa-camera"></i>
            </div> */}
          <input
            type="file"
            accept="image/*"
            onChange={handleLogoChange}
            ref={fileInputRef}
            style={{ display: "none" }}
          />
        </div>

        <div className="company-header-right">
          <div className="company-title-line">
            <input
              type="text"
              className="company-name-input"
              value={corpName}
              onChange={(e) => setCorpName(e.target.value)}
              maxLength={50}
            />
          </div>

          <div className="company-basic-info">
            <div className="info-row">
              <div className="info-label">기업 형태</div>
              <input
                type="text"
                value={corpType}
                onChange={(e) => setCorpType(e.target.value)}
                maxLength={10}
              />
            </div>
            <div className="info-row">
              <div className="info-label">사업자 번호</div>
              <input
                type="text"
                value={corpRegNo}
                onChange={(e) => setCorpRegNo(e.target.value)}
                maxLength={10}
              />
            </div>

            <div className="info-row">
              <div className="info-label">대표명</div>
              <input
                type="text"
                value={corpCeoName}
                onChange={(e) => setCorpCeoName(e.target.value)}
                maxLength={20}
              />
            </div>
            <div className="address-info-row">
              <div className="info-label">주소</div>
              <div className="input-button-wrap">
                <input
                  type="text"
                  value={corpAddr}
                  onChange={(e) => setCorpAddr(e.target.value)}
                  maxLength={100}
                />
                <button
                  type="button"
                  className="btn-address"
                  onClick={handleAddressSearch}
                >
                  주소검색
                </button>
              </div>
            </div>

            <div className="info-row">
              <div className="info-label">개업일자</div>
              <input
                type="date"
                value={corpOpenDate}
                onChange={(e) => setCorpOpenDate(e.target.value)}
              />
            </div>
            <div className="info-row">
              <div className="info-label">홈페이지</div>
              <input
                type="text"
                value={corpHomeLink}
                onChange={(e) => setCorpHomeLink(e.target.value)}
                maxLength={100}
              />
            </div>
          </div>
        </div>
      </div>

      <div className="company-section">
        <h3>주요사업</h3>
        <textarea
          value={corpBm}
          onChange={(e) => setCorpBm(e.target.value)}
          maxLength={100}
        ></textarea>
      </div>

      <div className="company-section">
        <h3>기업상세</h3>
        <textarea
          value={corpDetail}
          onChange={(e) => setCorpDetail(e.target.value)}
          maxLength={1000}
        ></textarea>
      </div>

      <div className="company-section">
        <h3>복리후생</h3>
        <textarea
          value={corpBenefit}
          onChange={(e) => setCorpBenefit(e.target.value)}
          maxLength={100}
        ></textarea>
        <textarea
          value={corpBenefitDetail}
          onChange={(e) => setCorpBenefitDetail(e.target.value)}
          maxLength={2000}
        ></textarea>
      </div>
      <FloatButton
        buttons={FLOAT_BUTTON_PRESETS.editAndCancel2(handleEdit, handleCancel)}
      />
    </main>
  );
};

export default EditCompanyPage;
