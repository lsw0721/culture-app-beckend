import pandas as pd

# 사전 생성된 추천 결과 파일 로드
# 모델에서 export한 결과
final_recommendations = pd.read_csv("final_recommendations.csv")

def get_recommendations(user_id: int, top_n: int = 3):
    # 해당 사용자에 대한 추천만 필터링
    user_df = final_recommendations[final_recommendations['member_id'] == user_id]
    
    # final_score 기준 상위 N개 추출
    top_n_df = user_df.sort_values(by='final_score', ascending=False).head(top_n)
    
    # 추천 콘텐츠 ID 리스트 반환
    return top_n_df['content_detail_id'].tolist()


