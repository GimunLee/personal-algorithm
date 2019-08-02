package samsung;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Solution_5656_벽돌깨기 {
	static int[] dr = { -1, 1, 0, 0 };
	static int[] dc = { 0, 0, -1, 1 };
	private static int[][] map; // 원본 맵 데이터를 저장하는 변수
	private static int N; // 구슬을 쏘는 횟수
	private static int W; // 맵의 가로 범위 제한 
	private static int H; // 맵의 세로 범위 제한
	private static int[][] shooted; // 구슬로 인해 폭발을 하는지 체크하는 변수
	private static int[] maxHByWIndex; // 해당 열에 있는 블록의 수를 저장하는 변수
	private static int[][] queue; // 구슬이 한번 떨어졌을 때, 영향을 받는 블록을 한번에 처리하기 위한 큐
	private static int blockCnt; // 블록의 총 갯수를 저장하는 변수
	private static int ans; // 정답을 저장할 변수
	private static int ans_tmp; // 갱신할 임시 정답 변수

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); 
		StringBuilder sb = new StringBuilder(); // 정답을 저장했다가 출력하는 변수
		int TC = Integer.parseInt(br.readLine().trim()); // Test Case 수

		for (int tc = 1; tc <= TC; tc++) {
			StringTokenizer st = new StringTokenizer(br.readLine(), " ");
			N = Integer.parseInt(st.nextToken()); // 구슬을 쏠 수 있는 횟수
			W = Integer.parseInt(st.nextToken()); // 맵의 가로 범위 제한 / Width (Column)
			H = Integer.parseInt(st.nextToken()); // 맵의 세로 범위 제한 / Height (Row)

			map = new int[H][W]; // 블록의 상태를 저장하는 맵 변수
			maxHByWIndex = new int[W]; // 맵의 한 열에 존재하는 블록의 개수를 저장하는 변수
			 
			queue = new int[1000][2]; // 구슬이 한번 떨어졌을 때, 영향을 받는 블록을 한번에 처리하기 위한 큐 
			shooted = new int[H][W]; // 해당 블록이 맞았는지 안 맞았는지, 갱신하는 변수

			set = new int[N]; // 구슬을 떨어뜨릴 열을 고를 변수 (중복순열)
			blockCnt = 0; // 블록의 총 갯수를 저장하는 변수
			ans = Integer.MAX_VALUE; // 각 테스트케이스 별 정답을 갱신하는 변수
			
			for (int r = 0; r < H; r++) {
				st = new StringTokenizer(br.readLine(), " ");
				for (int c = 0; c < W; c++) {
					map[r][c] = Integer.parseInt(st.nextToken());
					if (map[r][c] >= 1) { // 블록이 차있으면, 
						maxHByWIndex[c] += 1; // 해당 열의 블록의 갯수를 저장할 변수
						blockCnt++; // 블록의 총 갯수를 올려줌
					}
				}
			} // end of for(input)

			solve(0); // 구슬을 N번 쏠 때, 떨어뜨릴 위치를 중복순열로 뽑음

			sb.append("#").append(tc).append(" ").append(ans).append("\n"); // 테스트케이스 별 정답 저장
		} 
		System.out.println(sb.toString()); // 정답 출력 
	} // end of main()

	private static int[] set; // 구슬을 떨어뜨릴, 중복 순열을 저장하는 변수

	/**
	 * 구슬을 떨어뜨릴 위치를 결정하고, 모두 뽑았을 때 구슬을 차례대로 떨어뜨려봄
	 * */
	private static void solve(int len) {
		if (len == N) { // 구슬을 쏠 위치를 모두 정했을 때,
			// 원본 데이터를 임시 변수에 저장
			int[][] map_tmp = new int[H][W]; // 원본 맵을 복사해놓고, 블록을 터트릴 변수
			for (int i = 0; i < H; i++) { 
				map_tmp[i] = Arrays.copyOf(map[i], W);
			}
			int[] maxHByWIndex_tmp = new int[W]; // 블록을 터트릴때마다 갱신되는 임시 변수
			maxHByWIndex_tmp = Arrays.copyOf(maxHByWIndex, W); 
			// --

			ans_tmp = blockCnt;
			dropBall(map_tmp, maxHByWIndex_tmp, set); // 뽑은 구슬을 떨어뜨릴 위치로 구슬을 떨어뜨려봄 
			ans = ans > ans_tmp ? ans_tmp : ans; // 정답 변수 갱신
			return;
		}

		for (int i = 0; i < W; i++) {
			set[len] = i; // 중복 순열 저장 
			solve(len + 1); // 중복 순열 재귀 호출
		}
	}

	/**
	 * 구슬을 떨어뜨려보는 함수
	 * */
	private static void dropBall(int[][] map_tmp, int[] maxHByWIndex_tmp, int[] set) {
		
		// 해당 위치에 블럭이 있을 때 까지 구슬이 내려가는 것을 탐색하지 않고, 최대 높이를 저장해놓음으로 처리
		for (int i = 0; i < set.length; i++) { // 구슬 횟수만큼 반복
			boolean[] isBomb = new boolean[W];
			int c = set[i]; // 떨어뜨리는 위치 (Column)
			if(maxHByWIndex_tmp[c] <= 0 ) { // 해당 열에 남은 블록이 없다면 떨어뜨리지 않음
				continue;
			}
			
			// 해당 위치의 가장 높은 위치 (배열에서는 역순이므로 최대 높이에서 블록을 빼주면 시작할 인덱스가 나옴)
			int r = H - maxHByWIndex_tmp[c];
			
			int front = -1; // queue의 poll을 할 때, 인덱스 변수
			int rear = -1; // queue의 add를 할 때, 인덱스 변수

			queue[++rear][0] = r; // 구슬이 처음 만나는 블록의 세로 위치를 add
			queue[rear][1] = c;  // 구슬이 처음 만나는 블록의 가로 위치를 add

			while (front != rear) {
				// queue.poll();
				int rr = queue[++front][0];
				int cc = queue[front][1];
				int bombRange = map_tmp[rr][cc] - 1; // 폭발 범위
				shooted[rr][cc] += 1; // queue에 들어간 블록의 인덱스는 무조건 1 이상이므로, 첫 폭발은 무조건 올려줌
				isBomb[cc] = true;
				
				for (int bomb = 1; bomb <= bombRange; bomb++) { // 폭발 범위만큼 탐색
					for (int di = 0; di < dr.length; di++) {
						int nR = rr + dr[di] * bomb; // 폭발시켰을 때의 블록의 세로 인덱스
						int nC = cc + dc[di] * bomb; // 폭발시켰을 때의 블록이 가로 인덱스

						if (nR < 0 || nC < 0 || nR >= H || nC >= W) { // 범위를 벗어나는 경우
							continue;
						}

						// 폭발했을때, 해당 자리에 블록이 있고, 한번도 폭발하지 않은 경우
						if (map_tmp[nR][nC] >= 1 && shooted[nR][nC] == 0) {
							// queue add
							queue[++rear][0] = nR; 
							queue[rear][1] = nC;
							shooted[nR][nC] += 1; // 맞은 것으로 처리
						}
					}
				}
			}

			int[] stack = new int[H]; // 이차원 배열은 역순이므로, stack의 FILO을 이용
			int top = -1; // stack의 인덱스 변수
			// 블록 제거하기 (제거하면서 shooted '0'으로 바꿔주기)
			for (int tc = 0; tc < W; tc++) {
				if(!isBomb[tc]) {
					continue;
				} 

				for (int tr = (H - maxHByWIndex_tmp[tc]); tr < H; tr++) {
					if (shooted[tr][tc] != 0 && map_tmp[tr][tc] != 0) {
						shooted[tr][tc] = 0;
						ans_tmp--;
					} else {
						stack[++top] = map_tmp[tr][tc];
					}
					map_tmp[tr][tc] = 0;
				}

				int tr = H - 1;
				maxHByWIndex_tmp[tc] = top + 1;
				while (top != -1) { // 블록 채우기
					map_tmp[tr--][tc] = stack[top--];
				}
			}
		}

	}

} // end of class
