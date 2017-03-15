set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [19051.66286	11701.15425	9708.099016	9077.704991	8434.995246];
y2 = [16343.74359	7250.695258	4840.887818	4192.202127	3536.563398];
y3 = [4699.944235	2005.765754	1190.830284	941.1294988	702.1582712];
y4 = [872.6229521	486.8822156	352.8290257	329.8513623	285.4495026];
y5 = [97.71511472	81.8072073	74.07620972	64.26564462	56.82764748];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;
p3 = semilogy(x, y3, '-kv');
hold on;
p4 = semilogy(x, y4, '-ks');
hold on;
p5 = semilogy(x, y5, '-kd');
hold on;

xlabel('k');
ylabel('time (ms)');

axis([0.5 5.5 0.0 180000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'4','5','6','7','8'});
%leg=legend('basic-g','basic-w','Inc-S','Inc-T','Dec', 1);
%set(leg,'edgecolor','white');

set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'FontSize', 12);
leg1 = legend([p1, p2],'basic-g','basic-w');
set(leg1,'edgecolor','white');

set(gca, 'LineWidth', 1.5);
ah=axes('position',get(gca,'position'),'visible','off');
set(gca, 'FontSize', 12);
leg2 = legend(ah,[p3, p4, p5],'Inc-S','Inc-T','Dec');
set(leg2,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);


